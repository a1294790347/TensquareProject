package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//需要对一个service方法添加事务时，加上@Transactional
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid){
        //先判断userid到friendid是否有数据，有就是重复添加好友
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if (friend != null){
            return 0;
        }
        //直接添加好友，让好友表中userid到friendid方向的type为0
        friend = new Friend();
        friend.setFriendid(friendid);
        friend.setUserid(userid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断从friendid到userid是否有数据，如果有，双方的状态都改为1
        if (friendDao.findByUseridAndFriendid(friendid, userid) != null){
            friendDao.updataIslike("1", userid, friendid);
            friendDao.updataIslike("1", friendid, userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断是否已经是非好友
        NoFriend nofriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if (nofriend != null){
            return 0;
        }
        nofriend = new NoFriend();
        nofriend.setFriendid(friendid);
        nofriend.setUserid(userid);
        noFriendDao.save(nofriend);
        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid到friendid这条数据
        friendDao.deleteFriend(userid, friendid);
        //更新friendid到userid的islike为0
        friendDao.updataIslike("0", friendid, userid);
        //为非好友表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
