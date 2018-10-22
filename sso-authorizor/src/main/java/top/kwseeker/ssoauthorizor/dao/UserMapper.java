package top.kwseeker.ssoauthorizor.dao;

import top.kwseeker.ssoauthorizor.pojo.User;

public interface UserMapper {

    void insertUser(User user);
    int countUserByName(String username);
    User selectUserByName(String username);
}
