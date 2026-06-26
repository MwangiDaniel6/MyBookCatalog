package com.example.mybookcatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private View detailView;
    private TextView tvDetailTitle, tvDetailAuthor, tvDetailGenre, tvDetailDescription;
    private ImageView ivDetailAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewBooks);
        detailView = findViewById(R.id.layoutBookDetail);
        
        tvDetailTitle = findViewById(R.id.textViewDetailTitle);
        tvDetailAuthor = findViewById(R.id.textViewDetailAuthor);
        tvDetailGenre = findViewById(R.id.textViewDetailGenre);
        tvDetailDescription = findViewById(R.id.textViewDetailDescription);
        ivDetailAuthor = findViewById(R.id.imageViewDetailAuthor);
        Button btnBack = findViewById(R.id.buttonBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Book> bookList = getBookList();
        BookAdapter adapter = new BookAdapter(bookList, this::showBookDetails);
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> hideBookDetails());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (detailView.getVisibility() == View.VISIBLE) {
                    hideBookDetails();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void showBookDetails(Book book) {
        tvDetailTitle.setText(book.getTitle());
        tvDetailAuthor.setText(getString(R.string.author_format, book.getAuthor()));
        tvDetailGenre.setText(getString(R.string.genre_format, book.getGenre()));
        tvDetailDescription.setText(book.getDescription());
        ivDetailAuthor.setImageResource(book.getImageResId());

        recyclerView.setVisibility(View.GONE);
        detailView.setVisibility(View.VISIBLE);
    }

    private void hideBookDetails() {
        detailView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private List<Book> getBookList() {
        List<Book> books = new ArrayList<>();
        
        books.add(new Book("The Last Ember", "Daniel Roth", "Historical Thriller", "A former archaeologist is pulled into a deadly conspiracy when ancient secrets buried beneath Rome resurface.", R.drawable.author_daniel_roth));
        books.add(new Book("Quantum Mirage", "Lila Chen", "Science Fiction", "In a future where time travel is illegal, a rogue physicist must choose between saving the world or saving her daughter.", R.drawable.author_lila_chen));
        books.add(new Book("Roots & Wings", "Maria Esteban", "Literary Fiction", "A moving generational story of a Cuban-American family searching for identity, belonging, and redemption.", R.drawable.author_maria_esteban));
        books.add(new Book("The Mind Sculptor", "Dr. Evan Shaw", "Psychology / Non-Fiction", "A groundbreaking look at neuroplasticity and how you can rewire your brain for success and happiness.", R.drawable.author_evan_shaw));
        books.add(new Book("Inkbound: Chronicles of the Lost Library", "J.R. Faulkner", "Fantasy / Adventure", "A young librarian discovers that ancient books can open portals to other worlds—but not all stories have happy endings.", R.drawable.author_jr_faulkner));
        books.add(new Book("Startup Savage", "Nicole Vega", "Business / Entrepreneurship", "A brutally honest guide to launching a tech startup in the real world, full of failures, pivots, and unexpected wins.", R.drawable.author_nicole_vega));
        books.add(new Book("Beneath Crimson Skies", "Tomasz Novak", "Historical Fiction / WWII", "The intertwined lives of resistance fighters, spies, and survivors during the Nazi occupation of Warsaw.", R.drawable.author_tomasz_novak));
        books.add(new Book("The Art of Stillness", "Tara Bell", "Self-Help / Mindfulness", "Learn how to find peace in a chaotic world by mastering the ancient wisdom of stillness.", R.drawable.author_tara_bell));
        books.add(new Book("Neon Ghosts", "Khalid Jones", "Urban Fantasy / Mystery", "A private investigator with the ability to see spirits uncovers a supernatural conspiracy beneath the city's neon lights.", R.drawable.author_khalid_jones));
        books.add(new Book("Eat Green, Live Clean", "Dr. Sanjay Patel", "Health & Wellness", "A practical guide to plant-based nutrition and detox living, backed by science and easy recipes.", R.drawable.author_sanjay_patel));
        return books;
    }

    // --- Inner Classes ---

    public static class Book implements Serializable {
        private final String title;
        private final String author;
        private final String genre;
        private final String description;
        private final int imageResId;

        public Book(String title, String author, String genre, String description, int imageResId) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.description = description;
            this.imageResId = imageResId;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getGenre() { return genre; }
        public String getDescription() { return description; }
        public int getImageResId() { return imageResId; }
    }

    public static class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
        private final List<Book> bookList;
        private final OnBookClickListener onBookClickListener;

        public interface OnBookClickListener {
            void onBookClick(Book book);
        }

        public BookAdapter(List<Book> bookList, OnBookClickListener onBookClickListener) {
            this.bookList = bookList;
            this.onBookClickListener = onBookClickListener;
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
            Book book = bookList.get(position);
            holder.textViewTitle.setText(book.getTitle());
            holder.textViewAuthor.setText(book.getAuthor());
            holder.imageViewAuthor.setImageResource(book.getImageResId());
            
            // Set click listener on title as per requirement
            holder.textViewTitle.setOnClickListener(v -> onBookClickListener.onBookClick(book));
            
            // Optional: Also allow clicking the image/item for better UX, 
            // but the requirement specifically mentions "clicking on the book title"
            holder.imageViewAuthor.setOnClickListener(v -> onBookClickListener.onBookClick(book));
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        public static class BookViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewTitle;
            final TextView textViewAuthor;
            final ImageView imageViewAuthor;

            public BookViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewBookTitle);
                textViewAuthor = itemView.findViewById(R.id.textViewBookAuthor);
                imageViewAuthor = itemView.findViewById(R.id.imageViewBookAuthor);
            }
        }
    }
}
