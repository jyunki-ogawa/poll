package com.skill_up.poll.repository;

import com.skill_up.poll.model.CommentModel;
import com.skill_up.poll.model.TopicModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcCommentRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

    }

    public List<CommentModel> fetchByTopicId(int topicId) {

        List<CommentModel> commentList;

        if (TopicModel.validateId(topicId) == 0) {
            //トピックIDがnullの場合はnullを返す
            commentList = null;
            return commentList;
        }

        commentList = jdbcTemplate.query(
                "select * from comments c inner join users u on c.user_id = u.username where c.topic_id = ? and c.body != '' and c.del_flg != 1 and u.del_flg != 1 order by c.id desc"
                , new DataClassRowMapper<>(CommentModel.class)
                ,topicId
        );
        return commentList;

    }

    public void insert(int topicId, int agree, String body, String userId)
    {


        jdbcTemplate.update("insert into comments (topic_id, agree, body, user_id) values (?, ?, ?, ?)",
                topicId,
                agree,
                body,
                userId
        );

    }




}
