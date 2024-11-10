package com.skill_up.poll.repository;

import com.skill_up.poll.model.CommentModel;
import com.skill_up.poll.model.TopicModel;
import com.skill_up.poll.model.UserModel;
import com.skill_up.poll.model.TopicModelWithNickname;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JdbcTopicRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTopicRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

    }

    public List<TopicModel> fetchByUserId(String userId) {

        List<TopicModel> topicList;
        UserModel userModel = new UserModel();
        if (userModel.validateId(userId) == 0) {
            //ユーザーIDがnullの場合はnullを返す
            topicList = null;
            return topicList;
        }

        String id = userId;
        topicList = jdbcTemplate.query("select * from topics where user_id = ? and del_flg != 1 order by id desc;", new DataClassRowMapper<>(TopicModel.class), id);
        return topicList;

    }

    public List<TopicModelWithNickname> fetchPublishedTopics() {

        List<TopicModelWithNickname> topicList;

        topicList = jdbcTemplate.query(
                "select * from topics t inner join users u on t.user_id = u.username where t.del_flg != 1 and u.del_flg != 1 and t.published = 1 order by t.id desc;"
                , new DataClassRowMapper<>(TopicModelWithNickname.class)
        );
        return topicList;

    }

    public TopicModelWithNickname fetchById(int topicId) {

        TopicModelWithNickname topic;

        if (TopicModel.validateId(topicId) == 0) {
            //ユーザーIDがnullの場合はnullを返す
            topic = null;
            return topic;
        }

        topic = jdbcTemplate.queryForObject("select * from topics t inner join users u on t.user_id = u.username where t.id = ? and t.del_flg != 1 and u.del_flg != 1 order by t.id desc", new DataClassRowMapper<>(TopicModelWithNickname.class), topicId);
        return topic;

    }

    public boolean incrementViewCount(int topicId)
    {

        if (TopicModel.validateId(topicId) == 0) {
            return false;
        }

        jdbcTemplate.update("update topics set views = views + 1 where id = ?",
            topicId
        );

        return true;

    }


    public Integer isUserOwnTopic(int topicId, String userId)
    {
        UserModel userModel = new UserModel();
        if (TopicModel.validateId(topicId) * userModel.validateId(userId) == 0) {

            return null;

        }

        Integer topicCount = jdbcTemplate.queryForObject("select count(1) from topics t where t.id = ? and t.user_id = ? and t.del_flg != 1;", Integer.class, topicId, userId);
        return topicCount;

    }

    public void update(int topicId, String title, int published)
    {


       jdbcTemplate.update("update topics set published = ?, title = ? where id = ?",
                published,
                title,
                topicId
        );

    }

    public void insert(String title, int published, String userId)
    {

        jdbcTemplate.update("insert into topics(title, published, user_id) values (?, ?, ?)",
                title,
                published,
                userId
        );

    }

    public int incrementLikesOrDislikes(int topicId, int agree)
    {

        int result = 0;
        CommentModel commentModel = new CommentModel();
        if (commentModel.validateTopicId(topicId)
                * commentModel.validateAgree(agree) == 0) {

            return 0;

        }

        if(agree == 0) {

            result = jdbcTemplate.update("update topics set likes = likes + 1 where id = ?",
                    topicId
            );

        } else {

            result = jdbcTemplate.update("update topics set dislikes = dislikes + 1 where id = ?",
                    topicId
            );

        }

        return result;

    }

}