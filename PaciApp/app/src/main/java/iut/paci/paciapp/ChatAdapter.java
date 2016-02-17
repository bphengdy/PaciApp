package iut.paci.paciapp;


        import java.util.ArrayList;
        import java.util.List;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.LinearLayout;
        import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<Comment> {

    private TextView bubble_text;
    private List<Comment> comments = new ArrayList<Comment>();
    private LinearLayout wrapper;

    @Override
    public void add(Comment object) {
        comments.add(object);
        super.add(object);
    }

    public ChatAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.comments.size();
    }

    public Comment getItem(int index) {
        return this.comments.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_chat, parent, false);
        }

        wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

        Comment c = getItem(position);

        bubble_text = (TextView) row.findViewById(R.id.comment);

        bubble_text.setText(c.comment);

        bubble_text.setBackgroundResource(c.left ? R.drawable.bubble2 : R.drawable.bubble1);
        wrapper.setGravity(c.left ? Gravity.START : Gravity.END);

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}