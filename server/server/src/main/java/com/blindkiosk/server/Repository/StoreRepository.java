package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.StoreModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends MongoRepository<StoreModel,String>{
    public StoreModel findByName(String name);
    List<StoreModel> findAllBy();
}

 /*private final JdbcTemplate jdbcTemplate;

    public Repository(DataSource dataSource){
        jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public Optional<StoreModel> findByName(String name) {
        List<StoreModel> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    private RowMapper<StoreModel> memberRowMapper() {
        return (rs, rowNum) -> {
            StoreModel member = new StoreModel();
            member.setId(rs.getLong("id"));
            member.setStoreName(rs.getString("name"));
            return member;
        };
    }*/

