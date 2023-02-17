package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.QHashtag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HashtagRepositoryImpl extends QuerydslRepositorySupport implements HashtagRepositoryCustom {

    public HashtagRepositoryImpl() {
        super(Hashtag.class);
    }

    @Override
    public List<String> findAllHashtagNames() {
        QHashtag hashtag = QHashtag.hashtag;

        return from(hashtag)
                .select(hashtag.hashtagName)
                .fetch();
    }

}
