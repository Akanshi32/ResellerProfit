package com.vansh.resellerprofit.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
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
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.vansh.resellerprofit.R;

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
import java.util.Date;
import java.util.Locale;

public class PdfCreateActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String FILE_FOLDER = "ResellerProfit";
    private static File file;
    private static final String filepath = Environment.getExternalStorageDirectory().getPath();
    private EditText _pdfBodyEDT;
    private boolean isPDFFromHTML = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity_main);

        _pdfBodyEDT = (EditText) findViewById(R.id.editText);
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
                Paragraph p1 = new Paragraph("Company ka title \n Learn how to create a PDF in android with image and dynamic text form User.");

                /* Create Set Font and its Size */
                Font paraFont= new Font(Font.FontFamily.HELVETICA);
                paraFont.setSize(16);
                p1.setAlignment(Paragraph.ALIGN_CENTER);
                p1.setFont(paraFont);

                //add paragraph to document
                document.add(p1);

                Paragraph p2 = new Paragraph(_pdfBodyEDT.getText().toString().trim());

                /* You can also SET FONT and SIZE like this */
                Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f, Color.GREEN);
                p2.setAlignment(Paragraph.ALIGN_CENTER);
                p2.setFont(paraFont2);

                document.add(p2);

                /* Inserting Image in PDF */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                Image myImg = Image.getInstance(stream.toByteArray());
                myImg.setAlignment(Image.MIDDLE);

                //add image to document
                document.add(myImg);

                addContent(document);

                //set footer
               /* Phrase footerText = new Phrase("This is an example of a footer");
                HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
                document.(pdfFooter);*/


            //Close the document
            document.close();
            Toast.makeText(this, "Pdf created successfully.", Toast.LENGTH_LONG).show();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            Log.e("PDF--->",  "exception", e);
        }
    }


    private static void addContent(Document document) throws DocumentException {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                Font.BOLD);
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                Font.BOLD);
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 2);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
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
