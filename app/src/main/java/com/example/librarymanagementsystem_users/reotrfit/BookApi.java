import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface BookApi {

    @GET("/books")
    Call<List<Book>> getBooks();

    @GET("/books/{id}")
    Call<Book> getBookById(@Path("id") long id);

    @GET("/books/search")
    Call<List<Book>> searchBooks(@Query("q") String query);

}
