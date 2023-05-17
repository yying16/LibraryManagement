package com.librarymanagement.controller;

//针对管理员admin的图书控制器

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.librarymanagement.domain.Book;
import com.librarymanagement.domain.Record;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books4admin")
public class BookController4Admin {
    @Autowired
    private BookService bookService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/renewsByPage/{currentPage}/{pageSize}") //更新分页查询(无条件)
    public IPage<Book> getAllBook(@PathVariable() Long currentPage,
                                  @PathVariable() Long pageSize) {
        return bookService.getAllBook(currentPage, pageSize);
    }

    @GetMapping("{isbn}") //根据书名查询
    public Book getBook(@PathVariable("isbn") String isbn) {
        return bookService.getBook(isbn);
    }

    @PostMapping //添加图书
    public void addBook(@RequestBody Book book) {
        //默认设置藏书数量100本
        book.setTotal(100);
        book.setRemain(100);
        bookService.InsertBook(book);
    }

    @PutMapping //修改图书(根据书名修改)
    public void updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
    }

    @DeleteMapping("{isbn}") //删除单个图书(根据isbn删除)
    public void deleteBook(@PathVariable("isbn") String isbn) {

        Book book = new Book();
        book.setIsbn(isbn);

        bookService.deleteByISBN(book);
    }

    @DeleteMapping("/ds/{ISBNs}") //删除批量图书(根据ISBNs删除)
    public void deleteBooks(@PathVariable("ISBNs") String ISBNsStr) {
        String[] ISBNs = ISBNsStr.split(" ");
        Book[] books = new Book[ISBNs.length];

        for (int i = 0; i < ISBNs.length; ++i) {
            books[i] = new Book();
            books[i].setIsbn(ISBNs[i]);
        }

        bookService.deleteBooks(books);
    }

    @GetMapping("/ByCondition4Book/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对书)
    public IPage<Book> queryBooksConditionally(@PathVariable("currentPage") Long currentPage,
                                               @PathVariable("pageSize") Long pageSize,
                                               @PathVariable("type") String type,
                                               @PathVariable("content") String content) {
        return bookService.queryBooksConditionally(type, content, currentPage, pageSize);
    }

    /*----------------借阅记录-------------------*/

    @GetMapping("/ByCondition4Record/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对借阅记录)
    public IPage<Record> queryRecordsConditionally(@PathVariable("currentPage") Long currentPage,
                                                   @PathVariable("pageSize") Long pageSize,
                                                   @PathVariable("type") String type,
                                                   @PathVariable("content") String content) {

        return recordService.getAllRecordsClassifiedPaginated(type, content, currentPage, pageSize);
    }

    @GetMapping("/recordsByPage/{currentPage}/{pageSize}") //借书记录分页查询(无条件)
    public IPage<Record> getAllRecord(@PathVariable("currentPage") Long currentPage,
                                      @PathVariable("pageSize") Long pageSize) {
        return recordService.getAllRecordsPaginated(currentPage, pageSize);
    }


    /*---------------异常记录----------------------------*/
    @GetMapping("/getAllAbnormalRecord/{currentPage}/{pageSize}") //借书记录分页查询(无条件)
    public IPage<Record> getAllAbnormalRecord(@PathVariable("currentPage") Long currentPage,
                                              @PathVariable("pageSize") Long pageSize) {
        return recordService.getAllAbnormalRecord(currentPage, pageSize);
    }

    @GetMapping("/getAbnormalRecordConditionally/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对借阅记录)
    public IPage<Record> getAbnormalRecordConditionally(@PathVariable("currentPage") Long currentPage,
                                                        @PathVariable("pageSize") Long pageSize,
                                                        @PathVariable("type") String type,
                                                        @PathVariable("content") String content) {

        return recordService.getAbnormalRecordConditionally(type, content, currentPage, pageSize);
    }
}
