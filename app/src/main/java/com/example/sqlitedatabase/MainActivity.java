package com.example.sqlitedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Database database;

    ListView lvCongViec;

    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.listViewCongViec);
        arrayCongViec = new ArrayList<>();

        adapter = new CongViecAdapter(this, R.layout.dong_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        // tao database Ghi chu
        database = new Database(this, "ghichu.sqlite", null, 1);

        // tao bang Cong Viec
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        // them du lieu
        // database.QueryData("Insert into CongViec values(null, 'Lap trinh')");

        getDaTaCongViec();




    }

    private void getDaTaCongViec() {
        // select data
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()) {
            String ten = dataCongViec.getString(1);
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id, ten));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            DialogThem();
        }


        return super.onOptionsItemSelected(item);
    }

    private void DialogThem() {
        Dialog dialog = new Dialog(this);

        // muốn bỏ title
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_them_cong_viec);

        EditText edtTen = (EditText) dialog.findViewById(R.id.editTextSua);
        Button btnThem = (Button) dialog.findViewById(R.id.btnCo);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnKhong);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // nhấn ở ngoài ko tắt
                // dialog.setCanceledOnTouchOutside(true);
                dialog.dismiss();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenCv = edtTen.getText().toString();
                if (tenCv.equals("")) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên công việc!!!", Toast.LENGTH_SHORT).show();
                } else {
                    database.QueryData("Insert into CongViec values(null, '" + tenCv + "')");
                    Toast.makeText(MainActivity.this, "Đã thêm!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getDaTaCongViec();
                }
            }
        });

        dialog.show();

    }

    public void DiaLogSuaCongViec(String ten, int id) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sua_cong_viec);

        EditText edtTenCv = dialog.findViewById(R.id.editTextSua);
        Button btnSua = dialog.findViewById(R.id.btnCo);
        Button btnHuy = dialog.findViewById(R.id.btnKhong);

        edtTenCv.setText(ten);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tencv = edtTenCv.getText().toString().trim();
                if (tencv.equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập Công Việc", Toast.LENGTH_SHORT).show();
                } else {
                    database.QueryData("UPDATE CongViec SET TenCV = '" + tencv + "' WHERE Id = '" + id + "'");
                    Toast.makeText(MainActivity.this, "Đã cập nhập", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getDaTaCongViec();
                }


            }
        });

        dialog.show();
    }

    public void XoaCongViec(int id) {
        database.QueryData("DELETE FROM CongViec WHERE Id = '" + id + "'");
        Toast.makeText(MainActivity.this, "Đã Xóa", Toast.LENGTH_SHORT).show();
        getDaTaCongViec();
    }

    // cách khác để xóa công việc

    public void DialogXoaCV (String tencv, int id) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa công việc " + tencv +" không?");

        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                XoaCongViec(id);
                getDaTaCongViec();
            }
        });


        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialogXoa.show();
    }

    public void DiaLogXoaCongViec(int id) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_xoa_cong_viec);

        Button btnCo = dialog.findViewById(R.id.btnCo);
        Button btnKhong = dialog.findViewById(R.id.btnKhong);

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XoaCongViec(id);
                dialog.dismiss();
            }
        });

        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}