package com.librarymanagement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.librarymanagement.domain.Record;
import com.librarymanagement.domain.User;
import com.librarymanagement.mapper.RecordMapper;
import com.librarymanagement.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RecordMapper recordMapper;

    //判断用户是否已注册
    public boolean checkUserHasRegister(String account) {
        try {
            return userMapper.selectById(account) != null;
        } catch (Exception e) {
            return false;
        }
    }

    //判断该用户是否已注册(已注册则返回true)
    public boolean CheckUser(User user) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.eq("password", user.getPassword());
            return userMapper.selectOne(wrapper) != null;
        } catch (Exception e) {
            return false;
        }
    }

    //根据账号和密码放回对应的User
    public User getUser(String account, String password) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("account", account);
            wrapper.eq("password", password);
            return userMapper.selectOne(wrapper);
        } catch (Exception e) {
            return null;
        }
    }

    //判断该用户是否为管理员
    public boolean isAdmin(User user) {
        try {
            return userMapper.selectById(user.getAccount()).getStatus() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    //注册用户，将用户的数据写入数据库(创建成功则返回true，失败则返回false)
    public boolean CreateUser(User user) {
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //修改用户密码
    public void changePassword(User user, String newPassword) {
        try {
            user = userMapper.selectById(user.getAccount());
            user.setPassword(newPassword);
            userMapper.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //返回指定用户的所有待归还书目(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端归还书目按钮（初始化）
    public List<Record> getAllUnreturnedRecords(User user) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.eq("state", "未归还");
            return recordMapper.selectList(wrapper);
        } catch (Exception e) {
            return null;
        }
    }

    //根据出版社返回指定用户的所有待归还书目(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端归还书目按钮（搜索类型选择出版社）
    public List<Record> getUnreturnedRecordsByPublisher(User user, String publisher) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.eq("state", "未归还");
            wrapper.eq("publisher", publisher);
            return recordMapper.selectList(wrapper);
        } catch (Exception e) {
            return null;
        }
    }

    //根据书名返回指定用户的所有待归还书目(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端归还书目按钮（搜索类型选择出版社）
    public List<Record> getUnreturnedRecordsByBookName(User user, String bookName) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.eq("state", "未归还");
            wrapper.eq("book_name", bookName);
            return recordMapper.selectList(wrapper);
        } catch (Exception e) {
            return null;
        }
    }

    //根据作者返回指定用户的所有待归还书目(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端归还书目按钮（搜索类型选择出版社）
    public List<Record> getUnreturnedRecordsByAuthor(User user, String Author) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.eq("state", "未归还");
            return recordMapper.selectList(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //借阅记录栏

    //返回指定用户的所有借阅记录(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端借阅记录按钮（初始化）
    public Record[] getAllRecords(User user) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            return null;
        }
    }

    //根据出版社返回指定用户的所有借阅记录(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端借阅记录按钮（搜索类型选择出版社）
    public Record[] getRecordsByPublisher(User user, String publisher) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.eq("publisher", "%"+publisher+"%");
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            return null;
        }
    }

    //根据书名返回指定用户的所有借阅记录(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端借阅记录按钮（搜索类型选择书名）
    public Record[] getRecordsByBookName(User user, String bookName) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.like("book_name", "%"+bookName+"%");
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            return null;
        }
    }

    //根据作者返回指定用户的所有借阅记录(放回值为包含所需信息的Record对象[书名,ISBN,作者,出版社,版次,借阅时间,归还时间])
    //对应前端借阅记录按钮（搜索类型选择作者）
    public Record[] getRecordsByAuthor(User user, String author) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.eq("account", user.getAccount());
            wrapper.like("author", "%"+author+"%");
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            return null;
        }
    }

}
