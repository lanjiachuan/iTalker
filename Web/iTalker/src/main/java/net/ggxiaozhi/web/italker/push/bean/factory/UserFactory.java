package net.ggxiaozhi.web.italker.push.bean.factory;

import net.ggxiaozhi.web.italker.push.bean.db.User;
import net.ggxiaozhi.web.italker.push.utils.Hib;
import net.ggxiaozhi.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.UUID;

/**
 * factory包下为全部业务逻辑的处理
 * 工程名 ： iTalker
 * 包名   ： net.ggxiaozhi.web.italker.push.bean.factory
 * 作者名 ： 志先生_
 * 日期   ： 2017/11
 * 功能   ：User相关的业务逻辑处理
 */
public class UserFactory {

    /**
     * 通过phone查询是否存在用户
     *
     * @param phone 注册的手机号
     * @return 返回查询的user
     */
    public static User findByPhone(String phone) {
        //此处可以用lambda表达式 由于熟练暂时不转化
        //Alt+Enter -> new Hib.Query<User>()与user转化lambda
        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                User user = (User) session.createQuery("from User where phone=:inPhone")
                        .setParameter("inPhone", phone).uniqueResult();
                return user;
            }
        });
    }

    /**
     * 通过name查询是否存在用户
     *
     * @param name 注册的手机号
     * @return 返回查询的user
     */
    public static User findByName(String name) {
        //此处可以用lambda表达式 由于熟练暂时不转化
        //Alt+Enter -> new Hib.Query<User>()与user转化lambda
        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                User user = (User) session.createQuery("from User where name=:name")
                        .setParameter("name", name).uniqueResult();
                return user;
            }
        });
    }

    /**
     * 用户注册
     * 注册的操作要先写入数据库，并返回数据库中的User信息
     * <p>
     * 调用此方法之前已经进行了重复的判断
     * 进入这个方法就说明用户信息没有重复的可以进行注册成功
     *
     * @param account  账户(这里为手机号)
     * @param password 密码
     * @param name     用户名
     * @return user 返回一个带Token的User
     */
    public static User register(String account, String password, String name) {

        account = account.trim();
        password = encodePassword(password);

        User user = createUser(account, password, name);

        if (user != null) {
            user = login(user);
        }
        return user;
        /*//账户就是手机号
        user.setPhone(account);
        user.setPassword(password);
        user.setName(name);

        *//*进行数据库的操作*//*
        //首先创建一个回话
        Session session = Hib.session();
        //创建一个事务
        session.beginTransaction();
        try {
            //保存操作
            session.save(user);
            //提交事务
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            //保存失败的情况下进行事务回滚
            session.getTransaction().rollback();
            return null;
        }
*/
    }

    /**
     * 注册部分的新建用户的逻辑
     *
     * @param account  手机号
     * @param password 加密后的密码
     * @param name     用户名
     * @return 返回一个用户
     */
    public static User createUser(String account, String password, String name) {
        User user = new User();
        //账户就是手机号
        user.setPhone(account);
        user.setPassword(password);
        user.setName(name);

        return Hib.query(session -> (User) session.save(user));

    }

    /**
     * 把一个用户进行登录操作
     * 本质上是进行token的操作
     *
     * @param user
     * @return
     */
    private static User login(User user) {

        //使用一个随机的UUID充当Token
        String newToken = UUID.randomUUID().toString();
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        //将新增加token的user存入到数据库或更新
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 加密密码
     *
     * @param password
     * @return
     */
    private static String encodePassword(String password) {
        //密码去除空格
        password = password.trim();
        //进行MD5非对称加密 加盐更安全 (加盐的话 盐也要存储)
        //加盐:指的是可以随机在字符串上加上当前的时间值或是其他随机的数值
        password = TextUtil.getMD5(password);

        //再进行一次对称的Base64加密 当然可以采取加盐的方案
        return TextUtil.encodeBase64(password);
    }
}