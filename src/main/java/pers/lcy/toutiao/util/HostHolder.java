package pers.lcy.toutiao.util;

import org.springframework.stereotype.Component;
import pers.lcy.toutiao.model.User;

/**
 * 保存线程的当前访问用户
 */
@Component
public class HostHolder {
    public static ThreadLocal<User> threadLocal = new ThreadLocal<User>();

    public void set(User user) {
        threadLocal.set(user);
    }

    public User get() {
        return threadLocal.get();
    }

    public void clear() {
        threadLocal.remove();
    }

}
