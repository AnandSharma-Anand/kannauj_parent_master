package app.added.kannauj.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.List;

import app.added.kannauj.Adapters.TimeTableLecturesAdapter;
import app.added.kannauj.Models.TimeTableModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.FeedItemDecoration;
import app.added.kannauj.databinding.FragmentTimeTableBinding;


public class TimeTableFragment extends Fragment {

    FragmentTimeTableBinding binding;
    private static final String TAG = "TimeTableFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_table, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<TimeTableModel> list = getArguments().getParcelableArrayList("list");
        binding.rvTimeTable.setAdapter(new TimeTableLecturesAdapter(getActivity(),list));
        binding.rvTimeTable.addItemDecoration((new FeedItemDecoration(0,70,7)));
    }
}
