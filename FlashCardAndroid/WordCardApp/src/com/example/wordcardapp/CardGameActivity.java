package com.example.wordcardapp;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.model.FlashCard;
import com.example.wordcardapp.model.FlashCards;
import com.example.wordcardapp.utils.Toaster;

public class CardGameActivity extends Activity {
	
	private TextView cardFrontTV;
	private EditText cardBackET;
	private TextView answerTV;
	private TextView lastResultTV;
	private TextView pointsTV;
	private Button nextCardButton;
	private Button checkAnswerButton;
	
	/**
	 * ID of owner category of displayed cards
	 */
	protected int ownerCategoryId;
	
	/**
	 * List of cards displayed
	 */
	protected int[] cards;
	
	/**
	 * Displayed card
	 */
	protected FlashCard card;
	
	/**
	 * Index of current card in cards
	 */
	protected int currentCardIndex = 0;
	
	/**
	 * Number of correct answers in this round
	 */
	protected int correctAnswers = 0;
	
	
	protected void refreshPoints() {
		pointsTV.setText(correctAnswers + "/" + cards.length);
	}

	/**
	 * Downloads card of given id and places it to cards list
	 * @param id
	 */
	protected void downloadCard(int id) {
		Call<FlashCard> call = 
				RetrofitWrapper.getCardAPI().getCard(id);
		call.enqueue(new Callback<FlashCard>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_cards));
			}

			@Override
			public void onResponse(Response<FlashCard> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					card = arg0.body();
					setDisplayedCard();
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_cards));
				}
			}

		});
	}
	
	protected void getCards(Call<FlashCards> call) {
		call.enqueue(new Callback<FlashCards>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_cards));
			}

			@Override
			public void onResponse(Response<FlashCards> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					cards = arg0.body().CardIDs;
					downloadCard(cards[0]);
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_cards));
				}
			}

		});
	}
	
	protected void setDisplayedCard() {
		// Show first card
		if(cards.length > 0) {
			cardFrontTV.setText(card.Front);
			refreshPoints();
			// Everything is ready to start the game
			// so enable check button
			checkAnswerButton.setEnabled(true);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_game);
		// Get view items
		cardFrontTV = (TextView)findViewById(R.id.cardFrontTV);
		cardBackET = (EditText)findViewById(R.id.cardBackET);
		answerTV = (TextView)findViewById(R.id.answerTV);
		lastResultTV = (TextView)findViewById(R.id.lastResultTV);
		pointsTV = (TextView)findViewById(R.id.pointsTV);
		nextCardButton = (Button)findViewById(R.id.nextCardButton);
		checkAnswerButton = (Button)findViewById(R.id.checkAnswerButton);
		// Disable buttons until cards downloads
		nextCardButton.setEnabled(false);
		checkAnswerButton.setEnabled(false);
		// Ask user to wait while cards are downloading
		Toast.makeText(this, getString(R.string.waiting), Toast.LENGTH_LONG).show();
		// Download cards to play on
		Call<FlashCards> call;
		// Check way of start from intent
		if(getIntent().getBooleanExtra("RandomCards", true)) {
			// Play on random cards
			call = RetrofitWrapper.getCardAPI().getRandomCards(5);
		}
		else {
			// Play on category
			// Get owner category's id from caller intent
			ownerCategoryId = getIntent().getIntExtra("CategoryId", 0);
			call = RetrofitWrapper.getCardAPI().getCards(ownerCategoryId);
		}
		getCards(call);
	}
	
	public void onCheckAnswer(View v) {
		// Disable button until next card
		checkAnswerButton.setEnabled(false);
		// Get user input
		String input = cardBackET.getText().toString();
		// Get current card
		FlashCard currentCard = card;
		// Remove accents
		String nonAccentedInput = Normalizer.normalize(input, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String nonAccentedCardBack = Normalizer.normalize(currentCard.Back, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		// Compare
		if(nonAccentedInput.equalsIgnoreCase(nonAccentedCardBack)) {
			// Match
			// Give a point
			correctAnswers++;
			refreshPoints();
			// Inform user
			lastResultTV.setText(getString(R.string.correct_answer));
		}
		else {
			// Inform user
			lastResultTV.setText(getString(R.string.wrong_answer));
		}
		// Display correct answer
		answerTV.setText(currentCard.Back);
		// Enable next button if there are some cards left
		if(currentCardIndex < cards.length-1) {
			nextCardButton.setEnabled(true);
		}
	}
	
	public void onNextCard(View v) {
		// Disable button until current answer is not checked
		nextCardButton.setEnabled(false);
		// Ask current
		cardFrontTV.setText(card.Front);
		// Clear labels
		answerTV.setText("");
		lastResultTV.setText("");
		cardBackET.setText("");
		currentCardIndex++;
		downloadCard(cards[currentCardIndex]);
	}
}
