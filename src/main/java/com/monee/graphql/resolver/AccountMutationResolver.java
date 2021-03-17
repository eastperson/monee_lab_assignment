package com.monee.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.monee.dao.PostDao;
import com.monee.model.Post;

import java.sql.SQLException;

public class AccountMutationResolver implements GraphQLMutationResolver {

    private PostDao postDao;

    public Post writePost(Long accountSeq,String title, String content) throws SQLException {
        Post post = new Post(title,content);
        return postDao.save(accountSeq,post).orElseThrow(NullPointerException::new);
    }

}
