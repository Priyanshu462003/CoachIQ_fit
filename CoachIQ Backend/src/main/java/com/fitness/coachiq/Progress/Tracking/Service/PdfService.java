package com.fitness.coachiq.Progress.Tracking.Service;

import com.fitness.coachiq.Progress.Tracking.Entity.ProgressAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
public class PdfService {


    public byte[] generateMonthlyReport(ProgressAnalysis analysis) {

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {


            PDPage page = new PDPage();
            document.addPage(page);


            PDFont font = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );


            PDFont boldFont = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );


            PDPageContentStream content =
                    new PDPageContentStream(document, page);


            int y = 750;


            y = writeLine(
                    content,
                    boldFont,
                    18,
                    y,
                    "CoachIQ Monthly Progress Report"
            );


            y -= 15;


            y = writeLine(
                    content,
                    font,
                    12,
                    y,
                    "User ID : " + analysis.getUserId()
            );


            y = writeLine(
                    content,
                    font,
                    12,
                    y,
                    "Report Month : " + analysis.getReportMonth()
            );


            y -= 20;


            y = writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Summary",
                    analysis.getSummary()
            );


            y = writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Strengths",
                    analysis.getStrengths()
            );


            y = writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Areas for Improvement",
                    analysis.getImprovements()
            );


            y = writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Workout Advice",
                    analysis.getWorkoutAdvice()
            );


            y = writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Nutrition Advice",
                    analysis.getNutritionAdvice()
            );


            y = writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Recovery Advice",
                    analysis.getRecoveryAdvice()
            );


            writeSection(
                    content,
                    boldFont,
                    font,
                    y,
                    "Motivation",
                    analysis.getMotivation()
            );


            content.close();


            document.save(outputStream);


            return outputStream.toByteArray();


        } catch (Exception e) {

            log.error("PDF generation failed", e);

            throw new RuntimeException(
                    "Failed to generate PDF",
                    e
            );
        }
    }




    private int writeSection(
            PDPageContentStream content,
            PDFont headingFont,
            PDFont bodyFont,
            int y,
            String heading,
            String text
    ) throws Exception {


        y = writeLine(
                content,
                headingFont,
                14,
                y,
                heading
        );


        y = writeParagraph(
                content,
                bodyFont,
                y,
                text
        );


        return y - 15;
    }





    private int writeParagraph(
            PDPageContentStream content,
            PDFont font,
            int y,
            String text
    ) throws Exception {


        if(text == null || text.isBlank()) {
            return y - 20;
        }


        text = sanitize(text);


        String[] words = text.split(" ");

        StringBuilder line = new StringBuilder();


        for(String word : words) {


            String testLine =
                    line + word;


            float width =
                    font.getStringWidth(testLine) / 1000;


            if(width > 500) {


                y = writeLine(
                        content,
                        font,
                        12,
                        y,
                        line.toString()
                );


                line = new StringBuilder();
            }


            line.append(word)
                    .append(" ");
        }


        if(!line.isEmpty()) {


            y = writeLine(
                    content,
                    font,
                    12,
                    y,
                    line.toString()
            );
        }


        return y;
    }





    private int writeLine(
            PDPageContentStream content,
            PDFont font,
            int size,
            int y,
            String text
    ) throws Exception {


        content.beginText();


        content.setFont(
                font,
                size
        );


        content.newLineAtOffset(
                50,
                y
        );


        content.showText(
                text
        );


        content.endText();


        return y - 20;
    }





    private String sanitize(String text) {

        return text
                .replace("•", "-")
                .replace("—", "-")
                .replace("–", "-")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("’", "'")
                .replaceAll("[^\\x00-\\x7F]", "");
    }

}