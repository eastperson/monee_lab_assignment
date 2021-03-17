package com.monee.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Post {

    private Long seq;
    private String title;
    private String content;
    private Account author = null;
    private Long revwCnt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<Reply> replyList;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public Long getRevwCnt() {
        return revwCnt;
    }

    public void setRevwCnt(Long revwCnt) {
        this.revwCnt = revwCnt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return seq.equals(post.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "Post{" +
                "seq=" + seq +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", revwCnt=" + revwCnt +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", replyList=" + replyList +
                '}';
    }
}
