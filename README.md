# SocialNetwork

Applicazione Spring Boot che implementa il modello dati di un semplice social network, con persistenza su PostgreSQL tramite Spring Data JPA.

## Stack tecnologico

- Java 17
- Spring Boot (Spring Web, Spring Data JPA)
- PostgreSQL
- Lombok
- Maven

## Modello dati

**Entità:**

- `User` — id (UUID), username (univoco), fullName, email
- `Post` — id (UUID), content, postDate, author (`User`)
- `Comment` — id (UUID), content, date, user (`User`), post (`Post`)
- `PostLike` — id (UUID), user (`User`), post (`Post`)

**Diagramma ER:**

![Diagramma ER](docs/er-diagram.png)

## Scelte di design sulle relazioni

- **Relazioni unidirezionali.** Ogni relazione è mappata solo con `@ManyToOne` sul lato che possiede la foreign key (`Post.author`, `Comment.user`, `Comment.post`, `PostLike.user`, `PostLike.post`). Non sono state aggiunte collezioni inverse (`@OneToMany`) su `User`/`Post`, perché non servono a navigare il grafo degli oggetti per questo esercizio: dati come "i like di un post" o "i post di un utente" vengono recuperati tramite query sulle repository, non attraversando l'oggetto Java. Questo evita anche i classici problemi delle relazioni bidirezionali in JPA (query N+1, loop infiniti nella serializzazione).

- **Chiavi primarie UUID.** Tutte le entità usano `UUID` (generato con `GenerationType.UUID`) invece di id numerici auto-incrementali, per non esporre identificativi sequenziali/indovinabili.

- **Tabella `users` invece di `user`.** `USER` è una parola riservata in PostgreSQL; per evitare conflitti con il parser SQL, l'entità `User` è mappata sulla tabella `users` (`@Table(name = "users")`).

- **Vincolo di unicità sui like duplicati.** Un utente non può mettere like più volte allo stesso post. Il vincolo è garantito su due livelli:
  1. **Database**: `@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))` sull'entità `PostLike`.
  2. **Service** (`PostLikeService.addLike`): prima di salvare, recupera tutti i like esistenti del post (`postLikeRepository.findByPost(post)`) e verifica con uno Stream (`anyMatch`) se l'utente ha già messo like; in tal caso lancia un'eccezione invece di salvare il duplicato.

## Come avviare il progetto

1. Creare un database PostgreSQL vuoto (es. `socialnetwork`) tramite pgAdmin.
2. Configurare `src/main/resources/application.properties` con url, username e password del proprio database.
3. Avviare l'applicazione (`./mvnw spring-boot:run` oppure dall'IDE). Grazie a `spring.jpa.hibernate.ddl-auto=update`, Hibernate crea automaticamente le tabelle a partire dalle entità.
4. All'avvio, il `CommandLineRunner` (`DataInitializer`) popola il database con utenti, post, commenti e like di esempio, e testa esplicitamente il vincolo anti-duplicati stampando in console l'esito.

## Screenshot

**Tabelle generate su pgAdmin:**

![Tabelle](docs/tables.png)

**Dati presenti nelle tabelle:**

![Dati](docs/data.png)
