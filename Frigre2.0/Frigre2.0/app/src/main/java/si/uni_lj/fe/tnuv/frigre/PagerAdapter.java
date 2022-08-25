package si.uni_lj.fe.tnuv.frigre;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int tabNum;

    public PagerAdapter(@NonNull FragmentManager fm, int tabNum) {
        super(fm);
        this.tabNum = tabNum;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsFragment();
            case 1:
                return new TournamentFragment();
            case 2:
                return new LadderFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.tabNum;
    }
}
