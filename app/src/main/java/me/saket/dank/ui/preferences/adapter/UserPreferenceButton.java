package me.saket.dank.ui.preferences.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.auto.value.AutoValue;
import com.jakewharton.rxrelay2.PublishRelay;
import javax.inject.Inject;
import me.saket.dank.R;
import me.saket.dank.ui.preferences.events.UserPreferenceClickListener;
import me.saket.dank.ui.preferences.events.UserPreferenceButtonClickEvent;

public interface UserPreferenceButton {

  @AutoValue
  abstract class UiModel implements UserPreferencesScreenUiModel {
    @Override
    public abstract long adapterId();

    @Override
    public Type type() {
      return Type.BUTTON;
    }

    public abstract String title();

    public abstract String summary();

    public abstract UserPreferenceClickListener clickListener();

    public static UiModel create(String title, String summary, UserPreferenceClickListener clickListener) {
      return new AutoValue_UserPreferenceButton_UiModel(title.hashCode(), title, summary, clickListener);
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_preferences_button_title) TextView titleView;
    @BindView(R.id.item_preferences_button_summary) TextView summaryView;

    private UiModel uiModel;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void set(UiModel uiModel) {
      this.uiModel = uiModel;
    }

    public void render() {
      titleView.setText(uiModel.title());
      summaryView.setText(uiModel.summary());
    }
  }

  class Adapter implements UserPreferencesScreenUiModel.ChildAdapter<UiModel, ViewHolder> {
    PublishRelay<UserPreferenceButtonClickEvent> itemClicks = PublishRelay.create();

    @Inject
    public Adapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
      ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.list_item_preference_button, parent, false));
      holder.itemView.setOnClickListener(o -> itemClicks.accept(UserPreferenceButtonClickEvent.create(
          holder.uiModel.clickListener(),
          holder.getLayoutPosition(),
          holder.getItemId())));
      return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, UiModel uiModel) {
      holder.set(uiModel);
      holder.render();
    }
  }
}
