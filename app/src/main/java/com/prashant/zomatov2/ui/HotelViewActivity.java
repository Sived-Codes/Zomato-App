package com.prashant.zomatov2.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.prashant.zomatov2.R;
import com.squareup.picasso.Picasso;

public class HotelViewActivity extends AppCompatActivity {

    ImageView hotelImage;
    TextView title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);

        hotelImage = findViewById(R.id.hotelImageView);
        title = findViewById(R.id.hotelName);
        desc = findViewById(R.id.descr);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String hotelTitle = extras.getString("title");
            String hotelImageURL = extras.getString("img");
            String hotelContact = extras.getString("contact");

            title.setText(hotelTitle);
            Picasso.get().load(hotelImageURL).into(hotelImage);

            String hotelDescription = generateHotelDescription(hotelTitle, hotelContact);
            desc.setText(hotelDescription);
        }
    }

    private String generateHotelDescription(String title, String contact) {
        StringBuilder description = new StringBuilder();

        // Introduction
        description.append("Welcome to ").append(title).append("!\n\n");

        // Overview
        description.append("Overview:\n");
        description.append("Experience luxury and comfort at our exquisite hotel. ");
        description.append("Our prime location, coupled with top-notch amenities, makes us the perfect choice for your stay. ");

        // Facilities
        description.append("\n\nFacilities:\n");
        description.append("Enjoy a wide range of facilities, including a state-of-the-art fitness center, ");
        description.append("an outdoor pool with a breathtaking view, and a spa for ultimate relaxation. ");

        // Accommodations
        description.append("\n\nAccommodations:\n");
        description.append("Our rooms and suites are designed with your comfort in mind. ");
        description.append("Each room is equipped with modern furnishings, high-speed internet, and a spacious work area. ");

        // Dining
        description.append("\n\nDining:\n");
        description.append("Savor delicious cuisine at our on-site restaurant, offering a diverse menu to satisfy every palate. ");
        description.append("Our expert chefs use fresh, local ingredients to create culinary delights. ");

        // Events and Meetings
        description.append("\n\nEvents and Meetings:\n");
        description.append("Host successful events and meetings in our well-equipped conference rooms and banquet halls. ");
        description.append("Our professional staff is dedicated to ensuring your event is a memorable success. ");

        // Local Attractions
        description.append("\n\nExplore the Area:\n");
        description.append("Discover nearby attractions, landmarks, and entertainment options. ");
        description.append("Our concierge can assist you in planning exciting excursions. ");

        // Contact Information
        description.append("\n\nContact Information:\n");
        description.append("For reservations and inquiries, please contact us at ").append(contact).append(". ");

        // Closing
        description.append("\n\nThank you for choosing ").append(title).append(" for your stay. ");
        description.append("We look forward to providing you with an unforgettable experience.");

        return description.toString();
    }

}
