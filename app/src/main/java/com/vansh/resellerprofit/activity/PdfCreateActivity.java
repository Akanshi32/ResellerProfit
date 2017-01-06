package com.vansh.resellerprofit.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.BillAdapter;
import com.vansh.resellerprofit.adapter.StockAdapter;
import com.vansh.resellerprofit.model.*;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.Consts;
import com.vansh.resellerprofit.utility.DialogUtil;
import com.vansh.resellerprofit.utility.Preferences;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itextpdf.text.Rectangle.BOX;

public class PdfCreateActivity extends AppCompatActivity {

    final ArrayList<String> list = new ArrayList<String>();

    String vat;
    String total;
    String totalvat;

    String vattin;
    String csttin;
    String compadd;
    String compname;

    String prodsp;
    String prodname;
    String prodquan;
    String billid;

    String custname;
    String custmobile;
    String customeremail;


    java.util.List<SoldId> uniqueid;
    String paymentmethod;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String FILE_FOLDER = "ResellerProfit";
    private static File file;
    private static final String filepath = Environment.getExternalStorageDirectory().getPath();
    private boolean isPDFFromHTML = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity_main);



        final ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);


        Intent intent = getIntent();
        final String id = intent.getStringExtra("prodid");


        Call<BillGenerate> call = apiService.generate(id);

                call.enqueue(new Callback<BillGenerate>() {
                    @Override
                    public void onResponse(Call<BillGenerate> call, final Response<BillGenerate> response) {
                        Bill bills = response.body().getBills();
                        custname=bills.getCustomerName().toString();
                        customeremail=bills.getCustomerEmail().toString();
                        custmobile=bills.getCustomerMobile().toString();


                        csttin=bills.getCstTin().toString();
                        vattin=bills.getVatTin().toString();
                        paymentmethod=bills.getPaymentMethod().toString();
                        uniqueid=bills.getSoldId();

                        SoldId si;
                        si=uniqueid.get(0);
                        billid=si.getId();
                        prodname=si.getItemId();
                        prodquan=si.getQuantity().toString();
                        prodsp=si.getSellingPrice().toString();

                            //list

                        vat=bills.getVatPercent().toString();
                        compname=bills.getCompanyName().toString();
                        compadd=bills.getAddress().toString();
                        billid=bills.getId().toString();
                        total=bills.getTotalSp().toString();
                        totalvat=bills.getVatAmount().toString();

                    }


                    @Override
                    public void onFailure(Call<BillGenerate> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("Error", t.toString());
                    }
                });




    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button2:
                if(Build.VERSION.SDK_INT >= 23)
                    if(!PermissionUtils.checkAndRequestPermission(PdfCreateActivity.this, REQUEST_CODE_ASK_PERMISSIONS, "You need to grant access to Write Storage", permission[0]))
                        return;
                isPDFFromHTML = false;
                createPdf();
                break;
            case R.id.button1:
                if(Build.VERSION.SDK_INT >= 23)
                    if(!PermissionUtils.checkAndRequestPermission(PdfCreateActivity.this, REQUEST_CODE_ASK_PERMISSIONS, "You need to grant access to Write Storage", permission[0]))
                        return;
                isPDFFromHTML = false;
                break;

        }
    }


    private void getFile() {
        file = new File(filepath, FILE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    private void createPdf()  {
        try {
            getFile();
            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);
            String timeStamp2 = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss", Locale.getDefault()).format(date);
            File myFile = new File(file.getAbsolutePath()+ File.separator + timeStamp + ".pdf");
            myFile.createNewFile();
            OutputStream output = new FileOutputStream(myFile);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setLinearPageMode();
            writer.setFullCompression();
            // document header attributes
            document.addAuthor("Reseller Profit");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("RP");
            document.addTitle("Reseller Profit");
            document.setPageSize(PageSize.A4);
            // left,right,top,bottom
            document.setMargins(36, 36, 36, 36);
            document.setMarginMirroring(true);
            // open document
            document.open();

            //Add content
               /* Create Paragraph and Set Font */
             /* Inserting Image in PDF */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.scaleAbsolute(50,50);
            myImg.setAlignment(Image.ALIGN_RIGHT);

            //add image to document
            document.add(myImg);
                Paragraph p1 = new Paragraph(compname);

                /* Create Set Font and its Size */
                Font paraFont= new Font(Font.FontFamily.HELVETICA);
                paraFont.setSize(20);
                p1.setAlignment(Paragraph.ALIGN_LEFT);
                p1.setFont(paraFont);
            document.add(p1);

            Paragraph p7 = new Paragraph(compadd+"\nVAT No: "+vattin+"\nTin No: "+csttin);

                /* Create Set Font and its Size */
            Font paraFont7= new Font(Font.FontFamily.HELVETICA);
            paraFont7.setSize(20);
            p7.setAlignment(Paragraph.ALIGN_LEFT);
            p7.setFont(paraFont7);



                //add paragraph to document
                document.add(p7);

            Paragraph p19 = new Paragraph("PAYMENT METHOD: "+paymentmethod);

                /* Create Set Font and its Size */
            Font paraFont19= new Font(Font.FontFamily.HELVETICA);
            paraFont19.setSize(20);
            p19.setAlignment(Paragraph.ALIGN_LEFT);
            p19.setFont(paraFont19);



                //add paragraph to document
                document.add(p19);

            Paragraph p5 = new Paragraph("BILLING DATE:"+ timeStamp2);

                /* Create Set Font and its Size */
            Font paraFont5= new Font(Font.FontFamily.HELVETICA);
            paraFont5.setSize(14);
            p5.setAlignment(Paragraph.ALIGN_RIGHT);
            p5.setFont(paraFont5);



            //add paragraph to document
            document.add(p5);

             /*   Paragraph p2 = new Paragraph(_pdfBodyEDT.getText().toString().trim());

                *//* You can also SET FONT and SIZE like this *//*
                Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f, Color.GREEN);
                p2.setAlignment(Paragraph.ALIGN_LEFT);
                p2.setFont(paraFont2);

                document.add(p2);
*/

            Paragraph p3 = new Paragraph("CUSTOMER DETAILS:");

                /* Create Set Font and its Size */
            Font paraFont3= new Font(Font.FontFamily.HELVETICA);
            paraFont3.setSize(16);
            p3.setAlignment(Paragraph.ALIGN_LEFT);
            p3.setFont(paraFont3);

            //add paragraph to document
            document.add(p3);



            Paragraph pname = new Paragraph(custname);

                /* You can also SET FONT and SIZE like this */
            Font paname= new Font(Font.FontFamily.COURIER,14.0f, Color.GREEN);
            pname.setAlignment(Paragraph.ALIGN_LEFT);
            pname.setFont(paname);

            document.add(pname);



            Paragraph p4 = new Paragraph(custmobile);

                /* Create Set Font and its Size */
            Font paraFont4= new Font(Font.FontFamily.HELVETICA);
            paraFont4.setSize(14);
            p4.setAlignment(Paragraph.ALIGN_LEFT);
            p4.setFont(paraFont4);

            //add paragraph to document
            document.add(p4);

            Paragraph padd = new Paragraph(customeremail);

                /* You can also SET FONT and SIZE like this */
            Font paadd= new Font(Font.FontFamily.COURIER,14.0f, Color.GREEN);
            padd.setAlignment(Paragraph.ALIGN_LEFT);
            padd.setFont(paadd);

            document.add(padd);




            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);
            Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                    Font.BOLD);

           /* Anchor anchor = new Anchor("First Chapter", catFont);
            anchor.setName("First Chapter");*/
            Paragraph p12 = new Paragraph("BILL ID:"+billid);
            Font paraFont12= new Font(Font.FontFamily.HELVETICA);
            paraFont12.setSize(18);
            p12.setAlignment(Paragraph.ALIGN_LEFT);
            p12.setFont(paraFont12);

            document.add(p12);


           Paragraph p16 = new Paragraph("\nIMEI:"+ Preferences.getPrefs(Consts.IMEI,PdfCreateActivity.this));
            Font paraFont16= new Font(Font.FontFamily.HELVETICA);
            paraFont16.setSize(16);
            p16.setAlignment(Paragraph.ALIGN_LEFT);
            p16.setFont(paraFont16);

            document.add(p16);

           /* Paragraph subPara = new Paragraph("Subcategory 1", subFont);*/
           /* Section subCatPart = catPart.addSection(subPara);*/
            //subCatPart.add(new Paragraph("BILL:"));


           /* subPara = new Paragraph("Subcategory 2", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph("Paragraph 1"));
            subCatPart.add(new Paragraph("Paragraph 2"));
            subCatPart.add(new Paragraph("Paragraph 3"));
*/
            // add a list
            /*createList(subCatPart);*/
            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 5);
           // subCatPart.add(paragraph);

            // add a table
          //  createTable(subCatPart);

            // now add all this to the document

            Paragraph p8 = new Paragraph("\n");
            Font paraFont8= new Font(Font.FontFamily.HELVETICA);
            paraFont8.setSize(16);
            p8.setAlignment(Paragraph.ALIGN_CENTER);
            p8.setFont(paraFont8);

            document.add(p8);
            PdfPTable table = new PdfPTable(3);

            // t.setBorderColor(BaseColor.GRAY);
            // t.setPadding(4);
            // t.setSpacing(4);
            // t.setBorderWidth(1);

            PdfPCell c1 = new PdfPCell(new Phrase("PRODUCT NAME"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("QUANTITY"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("PRICE"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);


            table.addCell(prodname);
            table.addCell(prodquan);
            table.addCell(prodsp);
            table.addCell(" ");
            table.addCell(" ");
            table.addCell(" ");

            table.addCell("SUBTOTAL(in Rupees):");
            table.addCell(" ");
            table.addCell(prodsp);
            table.addCell("VAT Amout");
            table.addCell("");
            table.addCell(totalvat);
            table.addCell("GRAND TOTAL:");
            table.addCell("");
            table.addCell(total);
            document.add(table);

           /* Paragraph p8 = new Paragraph("\nVAT%: "+ vat);
            Font paraFont8= new Font(Font.FontFamily.HELVETICA);
            paraFont8.setSize(16);
            p8.setAlignment(Paragraph.ALIGN_CENTER);
            p8.setFont(paraFont8);

            document.add(p8);
            Paragraph p9 = new Paragraph("\nVAT AMOUNT: "+ totalvat);
            Font paraFont9= new Font(Font.FontFamily.HELVETICA);
            paraFont9.setSize(16);
            p9.setAlignment(Paragraph.ALIGN_CENTER);
            p9.setFont(paraFont9);
            document.add(p9);
*/
            Paragraph p10 = new Paragraph("GRAND TOTAL : Rs."+total);
            Font paraFont10= new Font(Font.FontFamily.HELVETICA);
            paraFont10.setSize(22);
            p10.setAlignment(Paragraph.ALIGN_CENTER);
            p10.setFont(paraFont10);

            document.add(p10);

            Paragraph p13 = new Paragraph("For:" +compname);
            paraFont10.setSize(22);
            p13.setAlignment(Paragraph.ALIGN_RIGHT);
            p13.setFont(paraFont10);

            document.add(p13);

            Paragraph p14 = new Paragraph("\nSIGNATURE");
            paraFont10.setSize(20);
            p14.setAlignment(Paragraph.ALIGN_RIGHT);
            p14.setFont(paraFont10);

            document.add(p14);

            Paragraph p11 = new Paragraph("\nTERMS AND CONDITIONS:-\n1.Goods once sold will not be taken back or exchanged.\n2.Payment within due days otherwise @24% interest will be charged.\n3. We are not responsible for any breakage, shortage, leakage, damage or any kind of complaints as soon as goods are sold.");
            Font paraFont11= new Font(Font.FontFamily.HELVETICA);
            paraFont11.setSize(12);
            p11.setAlignment(Paragraph.ALIGN_LEFT);
            p11.setFont(paraFont11);

            document.add(p11);









            // Next section


            // Second parameter is the number of the chapter
           /* catPart = new Chapter(new Paragraph(anchor), 2);

            subPara = new Paragraph("Subcategory", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph("This is a very important message"));

            // now add all this to the document
            document.add(catPart);
*/


              //  addContent(document);


                //set footer
               /* Phrase footerText = new Phrase("This is an example of a footer");
                HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
                document.(pdfFooter);*/


            //Close the document
            document.close();
            Toast.makeText(this, "Pdf created successfully.", Toast.LENGTH_LONG).show();


            // YE WALA MESSAGE SEND KARNE KE LIYE HAI
           // Intent messageIntent = new Intent(Intent.ACTION_SEND);
           // messageIntent .setType("*/*");
           // messageIntent.putExtra(Intent.EXTRA_TEXT, "ThankYou For purchase at our store! Hope to see you again.");
           // startActivity(Intent.createChooser(messageIntent, "Send Message..."));

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent .setType("*/*");
            emailIntent .putExtra(Intent.EXTRA_EMAIL, new String[]{customeremail});
            emailIntent.putExtra(Intent.EXTRA_TEXT, "ThankYou For purchase at our store! Hope to see you again.");

            Uri uri = Uri.fromFile(myFile);
            emailIntent .putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Purchase at "+compname);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            Log.e("PDF--->",  "exception", e);
        }
    }






    private static void addContent(Document document) throws DocumentException {


    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("PRODUCT NAME"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("QUANTITY"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("TOTAL"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");
        subCatPart.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    createPdf();
                } else {
                    // Permission Denied
                    Toast.makeText(PdfCreateActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
