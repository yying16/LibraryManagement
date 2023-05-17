package com.librarymanagement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("t_book")
public class Book extends Model<Book> {
    @TableId(type = IdType.ASSIGN_ID)
    String isbn;        //书号
    String bookName;    //书名
    String author;      //作者
    String publisher;   //出版社
    String edition;     //版次
    float price;        //价格
    int total;          //藏书总数
    int remain;         //馆内剩余
    String place;       //存放位置
    boolean deleted;
    @TableField(exist = false)
    int temp;
}
