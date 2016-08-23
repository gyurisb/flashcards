package com.example.wordcardapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.dialogs.CardEditDialog;
import com.example.wordcardapp.dialogs.CardEditorCallback;
import com.example.wordcardapp.dialogs.CategoryEditDialog;
import com.example.wordcardapp.dialogs.CategoryEditorCallback;
import com.example.wordcardapp.dialogs.NameCallback;
import com.example.wordcardapp.dialogs.NameDialog;
import com.example.wordcardapp.model.Category;
import com.example.wordcardapp.model.FlashCard;
import com.example.wordcardapp.model.FlashCards;
import com.example.wordcardapp.utils.Toaster;

public class CardListActivity extends ListActivity {
	
	/**
	 * ID of owner category of displayed cards
	 */
	protected int ownerCategoryId;
	protected Category ownerCategory;
	
	/**
	 * List of cards displayed
	 */
	protected List<FlashCard> cards;
	
	/**
	 * Adapter for ListView
	 */
	protected ArrayAdapter<FlashCard> adapter;
	
	/**
	 * Last selected card
	 */
	protected FlashCard selectedCard = null;

	protected CardEditorCallback modifyCard = new CardEditorCallback() {

		@Override
		public void edit(String front, String back, String lang) {
			if(selectedCard==null) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.card_not_selected));
			}
			else {
				// Modify selected card
				selectedCard.Front = front;
				selectedCard.Back = back;
				selectedCard.Language = lang;
				Call<Void> call = RetrofitWrapper.getCardAPI()
						.modifyCard(selectedCard.ID, selectedCard);
				refreshCards(call);
			}
		}
		
	};
	
	protected CardEditorCallback createCard = new CardEditorCallback() {

		@Override
		public void edit(String front, String back, String lang) {
			FlashCard newCard = new FlashCard(front, back, lang, ownerCategoryId); 
			Call<Void> call = RetrofitWrapper.getCardAPI().createCard(newCard);
			refreshCards(call);
		}
		
	};
	
	protected NameCallback removeCard = new NameCallback() {
		
		@Override
		public void callback(String name) {
			// Search card by name from local list
			FlashCard card = searchCard(name);
			if(card==null) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.card_not_found));
				return;
			}
			// Remove category from server
			Call<Void> call = RetrofitWrapper.getCardAPI().removeCard(card.ID);
			refreshCards(call);
		}
	};
	
	/**
	 * Callback for category modification in dialog
	 */
	protected CategoryEditorCallback modifyCategory = new CategoryEditorCallback() {

		@Override
		public void edit(String name, String lang, boolean isPublic) {
			// Search category by name from local list
			Category cat = ownerCategory;
			if(cat==null) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.category_not_found));
				return;
			}
			// Modify category
			cat.Name = name;
			cat.Language = lang;
			cat.IsPublic = isPublic;
			// Send object to server
			Call<Void> call = RetrofitWrapper.getCategoryAPI().modifyCategory(cat.ID, cat);
			call.enqueue(new Callback<Void>(){
				@Override
				public void onFailure(Throwable arg0) {
					Toaster.error(getApplicationContext(), "Sikertelen módosítás.");
				}
				@Override
				public void onResponse(Response<Void> arg0, Retrofit arg1) {
					if (arg0.code() == 200) {
						Toaster.error(getApplicationContext(), "Módosítás kész.");
					}
					else {
						Toaster.error(getApplicationContext(), "Sikertelen módosítás.");
					}
				}
			});
		}		
	};
	
	protected void removeCategory() {
		// Remove category from server
		Call<Void> call = RetrofitWrapper.getCategoryAPI().removeCategory(ownerCategoryId);
		call.enqueue(new Callback<Void>(){
			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), "Sikertelen törlés.");
			}
			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if (arg0.code() == 200) {
					CardListActivity.this.finish();
				}
				else {
					Toaster.error(getApplicationContext(), "Sikertelen törlés.");
				}
			}
		});
	}
	
	protected void shareCategory() {
		// Get share key of category from server
		Call<String> call = RetrofitWrapper.getCategoryAPI().shareCategory(ownerCategoryId);
		call.enqueue(new Callback<String>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_categories));
			}

			@Override
			public void onResponse(Response<String> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
			        try {
						String key = arg0.body();
						AlertDialog.Builder alert = new AlertDialog.Builder(CardListActivity.this);
				        LayoutInflater inflater = CardListActivity.this.getLayoutInflater();
				        View layout=inflater.inflate(R.layout.dialog_category_share,null);       
				        alert.setView(layout);
				        final EditText et = (EditText)layout.findViewById(R.id.shareLinkET);
						et.setText(RetrofitWrapper.baseURL + "/categories/" + ownerCategoryId + "?" + "key=" + URLEncoder.encode(key, "UTF-8"));
				        alert.setPositiveButton(R.string.copy_to_clipboard, new DialogInterface.OnClickListener() {
				        	@Override
				        	public void onClick(DialogInterface dialog, int id) {
						        ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						        clipMan.setPrimaryClip(ClipData.newPlainText("label", et.getText()));
				        		dialog.dismiss();
				        	}
				        });
				        alert.create().show();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_categories));
				}
			}

		});
	}
	
	/**
	 * Enque call and get cards
	 * @param call Call to enque
	 */
	protected void refreshCards(Call<Void> call) {
		call.enqueue(new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_cards));
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					// Refresh cards
					getCards();
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_cards));
				}
			}

		});
	}
	
	/**
	 * @return first FlashCard from adapter with @param front
	 * 			or null if not found
	 */
	protected FlashCard searchCard(String front) {
		if(adapter==null) {
			return null;
		}
		for(int i=0;i<adapter.getCount();i++) {
			FlashCard card = adapter.getItem(i);
			if(card!=null && card.Front!=null && card.Front.equals(front)) {
				return card;
			}
		}
		return null;
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
					cards.add(arg0.body());
					adapter.notifyDataSetChanged();
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_cards));
				}
			}

		});
	}
	
	protected void getCards() {
		Call<FlashCards> call = 
				RetrofitWrapper.getCardAPI().getCards(ownerCategoryId);
		call.enqueue(new Callback<FlashCards>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_cards));
			}

			@Override
			public void onResponse(Response<FlashCards> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					// Init card list data structure
					cards = new ArrayList<>();
					// Bind data to layout
					adapter = new ArrayAdapter<FlashCard>(
							CardListActivity.this,
							R.layout.rowlayout, cards);
					setListAdapter(adapter);
					// Download cards
					for(int id : arg0.body().CardIDs) {
						// Asynchronous call!!
						downloadCard(id);
					}
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_cards));
				}
			}

		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get owner category's id from caller intent
		ownerCategoryId = getIntent().getIntExtra("CategoryId", 0);
		ownerCategory = (Category)getIntent().getSerializableExtra("Category");
		// Get card IDs of selected category from server
		getCards();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		selectedCard = (FlashCard) getListAdapter().getItem(position);
		// Modify card
		(new CardEditDialog(modifyCard))
			.show(getFragmentManager(), getString(R.string.modify_card_menu));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.create_card_menu) {
			(new CardEditDialog(createCard))
				.show(getFragmentManager(), getString(R.string.create_card_menu));
			return true;
		}
		else if (id == R.id.remove_card_menu) {
			(new NameDialog(removeCard))
				.show(getFragmentManager(), getString(R.string.remove_card_menu));
			return true;
		}
		else if (id == R.id.remove_category_menu) {
			removeCategory();
			return true;
		}
		else if (id == R.id.modify_category_menu) {
			(new CategoryEditDialog(modifyCategory))
				.show(getFragmentManager(), getString(R.string.modify_category_menu));
			return true;
		}
		else if (id == R.id.share_category_menu) {
			shareCategory();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
