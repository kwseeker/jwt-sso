package top.kwseeker.jwtservlet.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;

public class MybatisUtil {

    /**
     * 读取xml配置文件构造sqlSession工厂
     * @param xmlPath   xml配置文件
     * @return  sqlSession工厂
     */
    public static SqlSessionFactory getSqlSessionFactory(String xmlPath) {
        SqlSessionFactory sqlSessionFactory = null;
        try {
            InputStream  inputStream = Resources.getResourceAsStream(xmlPath);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    //获取自动提交的SQL会话
    public static SqlSession getSqlSession(String xmlPath) {
        return getSqlSession(xmlPath, true);
    }

    public static SqlSession getSqlSession(String xmlPath, boolean isAutoCommit) {
        return getSqlSessionFactory(xmlPath).openSession(isAutoCommit);
    }
}
