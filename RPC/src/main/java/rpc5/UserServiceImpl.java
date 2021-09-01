package rpc5;

import common.IUserService;
import common.User;

/**
 * 放在服务端的一个方法, 此时可以随意添加新的方法， User对象也可以改变属性
 * @author: guangxush
 * @create: 2020/05/03
 */
public class UserServiceImpl implements IUserService {
    @Override
    public User findUserById(Integer id) {
        return new User(id, "Sandy");
    }
}
