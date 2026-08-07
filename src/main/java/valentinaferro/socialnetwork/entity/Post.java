package valentinaferro.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;
    @Column(name = "post_date")
    private LocalDate postDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    public Post(String content, LocalDate postDate) {
        this.content = content;
        this.postDate = postDate;
    }
}
