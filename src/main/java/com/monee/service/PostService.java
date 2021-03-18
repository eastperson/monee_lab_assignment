package com.monee.service;

import com.monee.dao.LikeDao;
import com.monee.dao.PostDao;
import com.monee.model.Post;

import java.sql.SQLException;
import java.util.List;

public class PostService {

    private final PostDao postDao;
    private final LikeDao likeDao;

    public PostService(PostDao postDao, LikeDao likeDao){
        this.postDao = postDao; this.likeDao = likeDao;
    }

    public Post findById(Long seq) throws SQLException {
        return postDao.findByIdWithAll(seq).orElseThrow(NullPointerException::new);
    }

    public Post findByIdWithAccount(Long seq) throws SQLException {

        return postDao.findByIdWithAccount(seq).orElseThrow(NullPointerException::new);
    }

    public Post findByIdWithReplyList(Long seq) throws SQLException {

        return postDao.findByIdWithReplyList(seq).orElseThrow(NullPointerException::new);
    }

    public Post findByIdWithAll(Long seq) throws SQLException {
        return postDao.findByIdWithAll(seq).orElseThrow(NullPointerException::new);
    }

    public List<Post> findAll() throws SQLException {
        return postDao.findAll();
    }

    public Post save(String title, String content,Long accountSeq) throws SQLException {
        Post post = new Post(title,content);

        return postDao.save(accountSeq,post).orElseThrow(NullPointerException::new);
    }

    public Post updateTitle(Long seq,String title) throws SQLException {

        int result = postDao.updateTitle(seq,title);
        if(result < 1) return null;
        return postDao.findByIdWithAll(seq).orElseThrow(NullPointerException::new);
    }

    public Post updateContent(Long seq,String content) throws SQLException {

        int result = postDao.updateContent(seq,content);
        if(result < 1) return null;
        return postDao.findByIdWithAll(seq).orElseThrow(NullPointerException::new);
    }

    public boolean delete(Long seq) throws  SQLException{
        int result = postDao.delete(seq);

        return result > 0;
    }

    public boolean addLike(Long accountSeq, Long postSeq) throws SQLException {
        return likeDao.add(accountSeq,postSeq);
    }

    public boolean cancleLike(Long accountSeq, Long postSeq) throws SQLException {
        return likeDao.remove(accountSeq,postSeq);
    }

    public boolean isLikedPost(Long accountSeq, Long postSeq) throws SQLException{
        return likeDao.isLikedPost(accountSeq,postSeq);
    }

}
