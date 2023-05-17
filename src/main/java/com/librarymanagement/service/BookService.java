package com.librarymanagement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.librarymanagement.domain.Book;
import com.librarymanagement.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService extends ServiceImpl<BookMapper, Book> implements IService<Book> {

    @Autowired
    BookMapper bookMapper;

    //分页查询书本
    public IPage<Book> getAllBook(Long currentPage, Long pageSize) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", false);
        try{
            IPage<Book> page = new Page<Book>(currentPage, pageSize);
            bookMapper.selectPage(page,wrapper);
            return page;
        }catch (Exception e){
            return null;
        }
    }

    //分页查询书本(过滤库存为0)
    public IPage<Book> getAllBookWithFitter(Long currentPage, Long pageSize) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", false);
        wrapper.gt("remain",0);
        try{
            IPage<Book> page = new Page<>(currentPage, pageSize);
            bookMapper.selectPage(page,wrapper);
            return page;
        }catch (Exception e){
            return null;
        }
    }

    //条件分页查询书本
    public IPage<Book> queryBooksConditionally(String type, String content,Long currentPage, Long pageSize) {
        try{
            QueryWrapper<Book> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", false);
            if(!content.equals("")){
                if(type.equals("bookName")) {
                    wrapper.like("book_name","%"+content+"%");
                }else if(type.equals("author")) {
                    wrapper.like("author","%"+content+"%");
                }else if(type.equals("publisher")) {
                    wrapper.like("publisher","%"+content+"%");
                }
            }
            IPage<Book> page = new Page<>(currentPage, pageSize);
            bookMapper.selectPage(page,wrapper);
            return page;
        }catch (Exception e){
            return null;
        }
    }

    //条件分页查询书本(过滤)
    public IPage<Book> queryBooksConditionallyWithFitter(String type, String content,Long currentPage, Long pageSize) {
        try{
            QueryWrapper<Book> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", false);
            wrapper.gt("remain", 0);
            if(!content.equals("")){
                if(type.equals("bookName")) {
                    wrapper.like("book_name","%"+content+"%");
                }else if(type.equals("author")) {
                    wrapper.like("author","%"+content+"%");
                }else if(type.equals("publisher")) {
                    wrapper.like("publisher","%"+content+"%");
                }
            }
            IPage<Book> page = new Page<>(currentPage, pageSize);
            bookMapper.selectPage(page,wrapper);
            return page;
        }catch (Exception e){
            return null;
        }
    }

    //查询单本图书
    public Book getBook(String isbn){
        try {
            return bookMapper.selectById(isbn);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //新增图书
    public void InsertBook(Book book) {
        try {
            if(bookMapper.selectById(book.getIsbn())==null){
                bookMapper.insert(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //批量删除
    public void deleteBooks(Book[] books) {
        try {
            for (int i = 0; i < books.length; i++) {
                books[i].setDeleted(true);
                bookMapper.updateById(books[i]);
            }
        } catch (Exception e) {
        }
    }

    //根据ISBN删除
    public void deleteByISBN(Book book) {
        try {
            book.setDeleted(true);
            bookMapper.updateById(book);
        } catch (Exception e) {
        }
    }

    //修改图书
    public void updateBook(Book book) {
        try {
            bookMapper.updateById(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
