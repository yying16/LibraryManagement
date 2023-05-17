package com.librarymanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.librarymanagement.domain.Book;
import com.librarymanagement.domain.Record;
import com.librarymanagement.domain.User;
import com.librarymanagement.interceptor.UserInfoGetter;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.RecordService;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//针对普通用户(User)的图书控制器

@RestController
@RequestMapping("/books4user")
public class BookController4User {
    @Autowired
    private RecordService recordService;
    @Autowired
    private UserInfoGetter userInfoGetter;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    //---------------------------------借阅图书相关---------------------------------//


    @GetMapping("/n/{currentPage}/{pageSize}") //获取所有图书数据(不过滤)
    public IPage<Book> getAllBook(@PathVariable() Long currentPage,
                                  @PathVariable() Long pageSize) {

        return bookService.getAllBook(currentPage, pageSize);
    }

    @GetMapping("/y/{currentPage}/{pageSize}") //获取所有图书数据(过滤)
    public IPage<Book> getAllBookWithFitter(@PathVariable() Long currentPage,
                                            @PathVariable() Long pageSize) {
        return bookService.getAllBookWithFitter(currentPage, pageSize);

    }


    @GetMapping("/ByCondition4Book/n/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对书)(不过滤暂无库存)
    public IPage<Book> queryBooksConditionally(@PathVariable("currentPage") Long currentPage, @PathVariable("pageSize") Long pageSize, @PathVariable("type") String type, @PathVariable("content") String content) {
        return bookService.queryBooksConditionally(type, content, currentPage, pageSize);
    }

    @GetMapping("/ByCondition4Book/y/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对书)(过滤暂无库存)
    public IPage<Book> queryBooksConditionallyWithFitter(@PathVariable("currentPage") Long currentPage, @PathVariable("pageSize") Long pageSize, @PathVariable("type") String type, @PathVariable("content") String content) {
        return bookService.queryBooksConditionallyWithFitter(type, content, currentPage, pageSize);
    }

    //---------------------------------借阅记录相关---------------------------------//

    //用户查询自己以往所有的借阅记录(无条件)
    @GetMapping("/myRecord/{currentPage}/{pageSize}")
    public IPage<Record> getMyAllRecord(@PathVariable("currentPage") Long currentPage,
                                        @PathVariable("pageSize") Long pageSize) {
        return recordService.getMyAllRecord(userInfoGetter.getUser(), currentPage, pageSize);
    }


    //用户查询自己以往所有的借阅记录(根据查询条件)
    @GetMapping("/searchMyRecord/{currentPage}/{pageSize}/{type}/{content}")
    public IPage<Record> queryRecordsConditionally(@PathVariable("currentPage") Long currentPage,
                                                   @PathVariable("pageSize") Long pageSize,
                                                   @PathVariable("type") String type,
                                                   @PathVariable("content") String content) {
        return recordService.queryRecordsConditionally(userInfoGetter.getUser(), type, content, currentPage, pageSize); //输入的条件不合法(搜索类型没选、搜索内容不填，... etc),那直接返回全部借阅记录
    }

    //----------------------------------------归还书目界面-------------------------------------
    @GetMapping("/ByConditionForUnReturnRecord/{currentPage}/{pageSize}/{type}/{content}")
    public IPage<Record> getAllUnReturnRecordsConditionally(@PathVariable("currentPage") Long currentPage,
                                                            @PathVariable("pageSize") Long pageSize,
                                                            @PathVariable("type") String type,
                                                            @PathVariable("content") String content) {//归还书目界面的查询按钮

        return recordService.getAllUnReturnRecordsConditionally(userInfoGetter.getUser(), type, content, currentPage, pageSize);
    }

    //用户查看自己目前仍未归还书目的所有借阅记录(无条件)
    @GetMapping("/myRecord/unReturned/{currentPage}/{pageSize}")
    public IPage<Record> getAllUnreturnedRecords(@PathVariable() Long currentPage,
                                                 @PathVariable() Long pageSize) {
        return recordService.getAllUnreturnedRecords(userInfoGetter.getUser(), currentPage, pageSize);
    }
    //---------------------------------------操作相关-----------------------------------------------

    //用户借阅图书
    @PutMapping("/borrowBook")
    public void borrowBook(@RequestBody Book book) {
        recordService.borrowBooks(userInfoGetter.getUser(), book);
    }

    //用户续借图书
    @PutMapping("/deferRecord")
    public void deferRecord(@RequestBody Book book) {
        recordService.deferRecord(userInfoGetter.getUser(), book);
    }

    //用户挂失图书
    @PutMapping("/reportRecord")
    public void reportRecord(@RequestBody Book book) {
        recordService.reportRecord(userInfoGetter.getUser(), book);
    }

    //检查用户是否占有某本书未归还
    @PutMapping("/checkRecord")
    public boolean checkRecord(@RequestBody Book book) {
        return recordService.checkBook(userInfoGetter.getUser(), book);
    }

    //用户归还图书
    @PutMapping("/returnBook")
    public void returnBook(@RequestBody Book book) {
        recordService.returnBooks(userInfoGetter.getUser(), book);
    }
}
