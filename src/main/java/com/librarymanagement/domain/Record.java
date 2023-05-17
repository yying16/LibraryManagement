package com.librarymanagement.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("v_record")  //对应于数据库中的视图
public class Record {
    int recordId;
    String username;    //用户名
    String telephone;   //用户电话
    String email;       //用户邮箱
    String account;     //账号
    String bookName;    //书名
    String isbn;        //书号
    String author;      //作者
    String publisher;   //出版社
    String edition;     //版次
    String borrowTime;    //借阅时间
    String dateToReturn; //应还时间
    String returnTime;    //归还时间
    String state;   //归还状态
}
