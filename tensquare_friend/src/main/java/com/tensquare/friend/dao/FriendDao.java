package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String>{
    Friend findByUseridAndFriendid(String userid,String friendid);

    @Modifying
    @Query(value = "UPDATE tb_friend SET islike = ? WHERE userid = ? AND friendid = ?", nativeQuery=true)
    void updataIslike(String islike,String userid,String friendid);

    @Modifying
    @Query(value = "DELETE FROM tb_friend where userid = ? AND friendid = ?", nativeQuery = true)
    void deleteFriend(String userid, String friendid);
}
