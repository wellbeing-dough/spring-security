package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//crud 함수를 JpaRepository 가 들고 있음
//@Repository 라는 어노테이션이 없어도 IOC 된다. 이유는 JpaRepository 를 상속했기 때문에
//이제 필요한 곳에 @Autowired 해주면 된다
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
