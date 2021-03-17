package com.monee.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.monee.dao.PostDao;
import com.monee.model.Account;
import com.monee.model.Post;

import java.sql.SQLException;

public class PostResolver implements GraphQLResolver<Post> {
    private PostDao postDao;
    public Account getAuthor(Post post) throws SQLException {
        return postDao.findByIdWithAccount(post.getSeq()).get().getAuthor();
    }

}
