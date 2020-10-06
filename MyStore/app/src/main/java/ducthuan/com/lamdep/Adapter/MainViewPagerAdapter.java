package ducthuan.com.lamdep.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment>arrayFragment = new ArrayList<>();
    private ArrayList<String>arrayTitle = new ArrayList<>();

    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //click vào thằng nào trả về vị trí của fragment đó
        return arrayFragment.get(position);
    }

    //tra ra so luong fragment
    @Override
    public int getCount()  {
        return arrayFragment.size();
    }

    //add vao fragment va title
    public void addFragment(Fragment fragment, String title){
        arrayFragment.add(fragment);
        arrayTitle.add(title);
    }

    //ep vao viewpager roi thi ep vao tab, ten cua moi tile hien thi trong tab
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayTitle.get(position);
    }
}
