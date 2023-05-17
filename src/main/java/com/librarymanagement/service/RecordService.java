package com.librarymanagement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.librarymanagement.domain.Book;
import com.librarymanagement.domain.Record;
import com.librarymanagement.domain.User;
import com.librarymanagement.mapper.BookMapper;
import com.librarymanagement.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RecordService extends ServiceImpl<RecordMapper, Record> implements IService<Record> {
    @Autowired
    RecordMapper recordMapper;

    @Autowired
    BookMapper bookMapper;

    //分页查询借阅记录
    public IPage<Record> getAllRecordsPaginated(Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<>(currentPage, pageSize);
        recordMapper.selectPage(page, new QueryWrapper<>());
        return page;
    }

    public IPage<Record> getAllAbnormalRecord(Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<Record>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.ne("state", "未归还");
        wrapper.ne("state", "已归还");
        recordMapper.selectPage(page, wrapper);
        return page;
    }

    //用户-归还图书
    public IPage<Record> getAllUnreturnedRecords(User user, Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<Record>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account", user.getAccount());
        wrapper.eq("state", "未归还");
        recordMapper.selectPage(page, wrapper);
        return page;
    }

    public IPage<Record> queryRecordsConditionally(User user, String type, String content, Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account", user.getAccount());
        if (!content.equals("")) {
            if (type.equals("bookName")) {
                wrapper.like("book_name", "%" + content + "%");
            } else if (type.equals("author")) {
                wrapper.like("author", "%" + content + "%");
            } else if (type.equals("publisher")) {
                wrapper.like("publisher", "%" + content + "%");
            }
        }
        recordMapper.selectPage(page, wrapper);
        return page;
    }

    public IPage<Record> getAllRecordsClassifiedPaginated(String type, String content, Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        if (!content.equals("")) {
            if (type.equals("bookName")) {
                wrapper.like("book_name", "%" + content + "%");
            } else if (type.equals("author")) {
                wrapper.like("author", "%" + content + "%");
            } else if (type.equals("publisher")) {
                wrapper.like("publisher", "%" + content + "%");
            }
        }
        recordMapper.selectPage(page, wrapper);
        return page;
    }

    public IPage<Record> getAllUnReturnRecordsConditionally(User user, String type, String content, Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<Record>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account", user.getAccount());
        wrapper.eq("state", "未归还");
        if (!content.equals("")) {
            if (type.equals("bookName")) {
                wrapper.like("book_name", "%" + content + "%");
            } else if (type.equals("author")) {
                wrapper.like("author", "%" + content + "%");
            } else if (type.equals("publisher")) {
                wrapper.like("publisher", "%" + content + "%");
            }
        }
        recordMapper.selectPage(page, wrapper);
        return page;
    }

    public IPage<Record> getMyAllRecord(User user, Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<Record>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account", user.getAccount());
        recordMapper.selectPage(page, wrapper);
        return page;
    }

    //用户的一次借阅操作
    public void borrowBooks(User user, Book book) {
        try {
            Record record = new Record();
            record.setAccount(user.getAccount());
            record.setIsbn(book.getIsbn());
            record.setBorrowTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            record.setDateToReturn(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 2592000000L)));
            record.setState("未归还");
            book.setRemain(book.getRemain() - 1);
            recordMapper.insertRecord(record);
            bookMapper.updateById(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户的一次挂失操作
    public void reportRecord(User user, Book book) {
        try {
            Record t = new Record();
            t.setAccount(user.getAccount());
            t.setIsbn(book.getIsbn());
            Record record = recordMapper.getRecentRecordByISBNAndAccount(t);
            record.setState("挂失");
            recordMapper.updateRecord(record);
            book = bookMapper.selectById(book.getIsbn());
            book.setTotal(book.getTotal() - 1);   //总量减一
            bookMapper.updateById(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户的一次续借功能
    public void deferRecord(User user, Book book) {
        Record t = new Record();
        t.setAccount(user.getAccount());
        t.setIsbn(book.getIsbn());
        Record record = recordMapper.getRecentRecordByISBNAndAccount(t);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(record.getDateToReturn());
            String deferDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(date.getTime() + 2592000000L));
            record.setDateToReturn(deferDate);
            recordMapper.updateRecord(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户的一次还书()
    public void returnBooks(User user, Book book) {
        try {
            Record t = new Record();
            t.setAccount(user.getAccount());
            t.setIsbn(book.getIsbn());
            Record record = recordMapper.getRecentRecordByISBNAndAccount(t);
            record.setReturnTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            record.setState("已归还");
            recordMapper.updateRecord(record);
            book = bookMapper.selectById(book.getIsbn());
            book.setRemain(book.getRemain() + 1);
            bookMapper.updateById(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //检查用户是否可以再借阅该书（！true表示没有占有，可以继续借阅！）
    public boolean checkBook(User user, Book book) {
        try {
            Record t = new Record();
            t.setAccount(user.getAccount());
            t.setIsbn(book.getIsbn());
            Record[] records = recordMapper.getRecordByISBNAndAccount(t);
            if (records.length > 0) { //借过这本书
                t = recordMapper.getRecentRecordByISBNAndAccount(t);    //找到最近的借阅记录
                return !t.getState().equals("未归还");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public IPage<Record> getAbnormalRecordConditionally(String type, String content, Long currentPage, Long pageSize) {
        IPage<Record> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.ne("state", "未归还");
        wrapper.ne("state", "已归还");
        if (!content.equals("")) {
            if (type.equals("bookName")) {
                wrapper.like("book_name", "%" + content + "%");
            } else if (type.equals("author")) {
                wrapper.like("author", "%" + content + "%");
            } else if (type.equals("publisher")) {
                wrapper.like("publisher", "%" + content + "%");
            }
        }
        recordMapper.selectPage(page, wrapper);
        return page;
    }

}
