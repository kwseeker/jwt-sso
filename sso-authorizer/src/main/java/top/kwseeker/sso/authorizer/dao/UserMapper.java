package top.kwseeker.sso.authorizer.dao;

import top.kwseeker.sso.authorizer.pojo.User;

public interface UserMapper {

    void insertUser(User user);

    int countUserByName(String username);

    User selectUserByName(String username);
}
