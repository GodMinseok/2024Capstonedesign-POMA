package goinmul.sportsmanage.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerSupport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_support_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @ColumnDefault("false")
    private boolean secret;

    private LocalDateTime regdate;

    public static CustomerSupport createPost(User user, String title, String content, boolean secret) {
        CustomerSupport customerSupport = new CustomerSupport();
        customerSupport.setUser(user);
        customerSupport.setTitle(title);
        customerSupport.setContent(content);
        customerSupport.setSecret(secret);
        customerSupport.setRegdate(LocalDateTime.now());
        return customerSupport;
    }

}
