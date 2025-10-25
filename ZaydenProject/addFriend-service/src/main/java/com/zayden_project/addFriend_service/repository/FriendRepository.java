package com.zayden_project.addFriend_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayden_project.addFriend_service.entity.Friend;
import com.zayden_project.addFriend_service.enums.FriendStatus;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
    @Query("SELECT f FROM Friend f WHERE " + "(f.user1 = :user1 AND f.user2 = :user2) OR "
            + "(f.user1 = :user2 AND f.user2 = :user1)")
    Optional<Friend> findBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);

    @Query("SELECT f FROM Friend f WHERE f.status = 'PENDING' AND f.senderId != :currentUserId AND "
            + "(:currentUserId = f.user1 OR :currentUserId = f.user2)")
    List<Friend> findPendingReceived(@Param("currentUserId") String currentUserId);

    @Query("SELECT f FROM Friend f WHERE f.status = 'PENDING' AND f.senderId = :currentUserId")
    List<Friend> findPendingSent(@Param("currentUserId") String currentUserId);

    @Query(
            """
		SELECT CASE
			WHEN f.user1 = :userId THEN f.user2
			ELSE f.user1
		END
		FROM Friend f
		WHERE f.user1 = :userId OR f.user2 = :userId
	""")
    List<String> findAllRelatedUserIds(@Param("userId") String userId);

    @Query(
            """
		SELECT CASE
			WHEN f.user1 = :userId THEN f.user2
			ELSE f.user1
		END
		FROM Friend f
		WHERE f.senderId = :userId
		AND f.status IN :statuses
	""")
    List<String> findFollowingIdsBySender(
            @Param("userId") String userId, @Param("statuses") List<FriendStatus> statuses);
}
