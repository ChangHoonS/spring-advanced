package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 연관된 Entity를 SQL의 JOIN을 사용하여 한 번의 쿼리로 함께 조회하는 Fetch Join을 사용
    // JOIN FETCH 키워드를 사용
    // 연관된 Entity를 즉시 로딩하며 Lazy 설정과 무관하게 작동
    // 하지만 페이징이 불가능
//    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Todo t ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // 위의 FETCH JOIN을 EntityGraph로 바꾸어 보았다.
    // 맞는지는 사실 정확히는 잘 모르겠다 아직은....


    // 아래는 개별 조회 부분인데 모르고 아래 쿼리문을 주석처리하여 오류를 발생시켜서 복구 하였다.
    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    // 선언되고 쓰이지는 않는다고 해서 일단 주석처리(?)
//    int countById(Long todoId);
}

// 결론은 둘 다 사용이 가능하지만 페이징을 한다는 부분에서 EntityGraph를 사용하는 것이
// 더욱 적절하다고 판단
// 재사용성과 유지보수 측면에서 매우 유리 하도고 하지만 강의 내용에 의하면 유연성이 부족하며
// left outer join 만을 지원한다고 한다.
// 아직 정확히 모르겠지만 연관 관계가 깊을 경우 attributePaths에 모든 경로를 명시해야 한다고 함