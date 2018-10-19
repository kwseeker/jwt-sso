package top.kwseeker.jwtservlet.dao;


import top.kwseeker.jwtservlet.pojo.User;

public interface UserMapper {

    void insertUser(User user);
    int countUserByName(String username);
    User selectUserByName(String username);
}
