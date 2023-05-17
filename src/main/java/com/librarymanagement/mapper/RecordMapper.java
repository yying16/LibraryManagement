package com.librarymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.librarymanagement.domain.Record;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    //由于该mapper对应于数据库中的视图而不是record表
    @Insert("insert into t_record(account, isbn, borrow_time,date_to_return,return_time,state) values(#{account},#{isbn},#{borrowTime},#{dateToReturn},#{returnTime},#{state})")
    public void insertRecord(Record record);

    //根据账号更新借阅记录 √
    @Update("update t_record set account=#{account},ISBN=#{isbn},borrow_time=#{borrowTime},date_to_return=#{dateToReturn},return_time=#{returnTime},state=#{state} where record_id=#{recordId}")
    public void updateRecord(Record record);

    //获得用户对某本书的最后一次借阅记录
    @Select("select* from v_record where record_id = (select max(record_id)from v_record where account=#{account} and ISBN=#{isbn})")
    public Record getRecentRecordByISBNAndAccount(Record record);

    //获得某用户对某本书的所有借阅记录
    @Select("select* from v_record where account=#{account} and ISBN=#{isbn}")
    public Record[] getRecordByISBNAndAccount(Record record);
}
