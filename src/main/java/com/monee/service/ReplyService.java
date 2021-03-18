package com.monee.service;

import com.monee.dao.ReplyDao;
import com.monee.model.Post;
import com.monee.model.Reply;

import java.sql.SQLException;
import java.util.List;

public class ReplyService {

    private ReplyDao replyDao;

    public ReplyService(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    public Reply findById(Long seq) throws SQLException {
        return replyDao.findByIdWithAccount(seq).orElseThrow(NullPointerException::new);
    }
    public List<Reply> findByPostSeq(Long postSeq) throws SQLException {
        return replyDao.findByPostId(postSeq);
    }

    public Reply findByIdWithAccount(Long seq) throws SQLException {

        return replyDao.findByIdWithAccount(seq).orElseThrow(NullPointerException::new);
    }

    public List<Reply> findAll() throws SQLException {
        return replyDao.findAll();
    }

    public Reply save(Long accountSeq,Long postSeq,String content) throws SQLException {
        Reply reply = new Reply(content);

        return replyDao.save(accountSeq,postSeq,reply).orElseThrow(NullPointerException::new);
    }

    public Reply updateContent(Long seq, String content) throws SQLException {

        int result = replyDao.updateContent(seq,content);
        if(result < 1) return null;
        return replyDao.findByIdWithAccount(seq).orElseThrow(NullPointerException::new);
    }

    public boolean delete(Long seq) throws  SQLException{
        int result = replyDao.delete(seq);

        return result > 0;
    }



}
