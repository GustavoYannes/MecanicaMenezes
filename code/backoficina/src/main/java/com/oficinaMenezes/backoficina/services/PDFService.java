package com.oficinaMenezes.backoficina.services;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.oficinaMenezes.backoficina.models.dtos.pdf.OrcamentoPDFDto;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PDFService {

    public byte[] gerarOrcamentoPdf(OrcamentoPDFDto entrada) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Cores
            DeviceRgb corPrincipal = new DeviceRgb(102, 192, 193);
            DeviceRgb corTextoEscuro = new DeviceRgb(40, 40, 40);
            DeviceRgb corCinzaClaro = new DeviceRgb(245, 245, 245);
            DeviceRgb corLinha = new DeviceRgb(220, 220, 220);

            // Formatadores
            NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Margens
            document.setMargins(30, 30, 30, 30);

            // =========================
            // CABEÇALHO
            // =========================
            Table cabecalho = new Table(UnitValue.createPercentArray(new float[]{2, 2}))
                    .useAllAvailableWidth();

            Cell esquerda = new Cell().setBorder(Border.NO_BORDER);

            esquerda.add(new Paragraph("MECÂNICA\nMENEZES")
                    .setFontSize(22)
                    .setFontColor(corTextoEscuro)
                    .setMarginBottom(10));

            esquerda.add(new Paragraph("Orçamento de serviços automotivos")
                    .setFontSize(10)
                    .setFontColor(corPrincipal));

            Cell direita = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT);

            direita.add(new Paragraph("Mecânica Menezes")
                    .setFontSize(12)
                    .setFontColor(corTextoEscuro));

            direita.add(new Paragraph("Atendimento especializado em manutenção automotiva")
                    .setFontSize(9)
                    .setFontColor(ColorConstants.DARK_GRAY));

            direita.add(new Paragraph(" ").setMarginBottom(5));

            direita.add(new Paragraph("ORÇAMENTO")
                    .setFontSize(13)
                    .setFontColor(corPrincipal));

            cabecalho.addCell(esquerda);
            cabecalho.addCell(direita);

            document.add(cabecalho);
            document.add(new Paragraph(" ").setMarginBottom(8));

            // =========================
            // DADOS DO CLIENTE E VEÍCULO
            // =========================
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();

            Cell infoEsquerda = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setPadding(10)
                    .setBackgroundColor(corCinzaClaro);

            infoEsquerda.add(new Paragraph("CLIENTE")
                    .setFontColor(corPrincipal)
                    .setFontSize(10));
            infoEsquerda.add(new Paragraph(entrada.getNomeCliente())
                    .setFontSize(11)
                    .setFontColor(corTextoEscuro));

            Cell infoDireita = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setPadding(10)
                    .setBackgroundColor(corCinzaClaro);

            infoDireita.add(new Paragraph("VEÍCULO")
                    .setFontColor(corPrincipal)
                    .setFontSize(10));
            infoDireita.add(new Paragraph("Modelo: " + entrada.getModelo()));
            infoDireita.add(new Paragraph("Placa: " + entrada.getPlaca()));
            infoDireita.add(new Paragraph("Ano: " + entrada.getAno()));
            infoDireita.add(new Paragraph("KM: " + entrada.getKm()));

            infoTable.addCell(infoEsquerda);
            infoTable.addCell(infoDireita);

            document.add(infoTable);
            document.add(new Paragraph(" ").setMarginBottom(10));

            // =========================
            // TABELA DE SERVIÇOS
            // =========================
            Table tabela = new Table(UnitValue.createPercentArray(new float[]{4, 1.5f, 2, 2}))
                    .useAllAvailableWidth();

            tabela.addHeaderCell(
                    new Cell()
                            .add(new Paragraph("DESCRIÇÃO"))
                            .setBackgroundColor(corPrincipal)
                            .setFontColor(ColorConstants.WHITE)
                            .setBorder(Border.NO_BORDER)
            );

            tabela.addHeaderCell(
                    new Cell()
                            .add(new Paragraph("QTD"))
                            .setBackgroundColor(corPrincipal)
                            .setFontColor(ColorConstants.WHITE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBorder(Border.NO_BORDER)
            );

            tabela.addHeaderCell(
                    new Cell()
                            .add(new Paragraph("VALOR UNIT."))
                            .setBackgroundColor(corPrincipal)
                            .setFontColor(ColorConstants.WHITE)
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setBorder(Border.NO_BORDER)
            );

            tabela.addHeaderCell(
                    new Cell()
                            .add(new Paragraph("TOTAL"))
                            .setBackgroundColor(corPrincipal)
                            .setFontColor(ColorConstants.WHITE)
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setBorder(Border.NO_BORDER)
            );

            BigDecimal total = BigDecimal.ZERO;

            if (entrada.getServicos() != null && !entrada.getServicos().isEmpty()) {
                for (Servico servico : entrada.getServicos()) {
                    tabela.addCell(
                            new Cell()
                                    .add(new Paragraph(servico.getNome()))
                                    .setBorderBottom(new SolidBorder(corLinha, 1))
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderLeft(Border.NO_BORDER)
                                    .setBorderRight(Border.NO_BORDER)
                    );

                    tabela.addCell(
                            new Cell()
                                    .add(new Paragraph(String.valueOf(servico.getQuantidade())))
                                    .setTextAlignment(TextAlignment.CENTER)
                                    .setBorderBottom(new SolidBorder(corLinha, 1))
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderLeft(Border.NO_BORDER)
                                    .setBorderRight(Border.NO_BORDER)
                    );

                    tabela.addCell(
                            new Cell()
                                    .add(new Paragraph(moeda.format(servico.getValorUnidade())))
                                    .setTextAlignment(TextAlignment.RIGHT)
                                    .setBorderBottom(new SolidBorder(corLinha, 1))
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderLeft(Border.NO_BORDER)
                                    .setBorderRight(Border.NO_BORDER)
                    );

                    tabela.addCell(
                            new Cell()
                                    .add(new Paragraph(moeda.format(servico.valorTotal())))
                                    .setTextAlignment(TextAlignment.RIGHT)
                                    .setBorderBottom(new SolidBorder(corLinha, 1))
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderLeft(Border.NO_BORDER)
                                    .setBorderRight(Border.NO_BORDER)
                    );

                    total = total.add(servico.valorTotal());
                }
            } else {
                tabela.addCell(
                        new Cell(1, 4)
                                .add(new Paragraph("Nenhum serviço cadastrado."))
                                .setTextAlignment(TextAlignment.CENTER)
                                .setFontColor(ColorConstants.GRAY)
                                .setPadding(10)
                );
            }

            document.add(tabela);
            document.add(new Paragraph(" ").setMarginBottom(10));

            // =========================
            // TOTAL E DATAS
            // =========================
            Table rodapeInfo = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                    .useAllAvailableWidth();

            Cell datasCell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setPaddingTop(10);

            datasCell.add(new Paragraph("INFORMAÇÕES")
                    .setFontColor(corPrincipal)
                    .setFontSize(10));

            if (entrada.getDataEntrada() != null) {
                datasCell.add(new Paragraph("Data de entrada: " + entrada.getDataEntrada().format(dataFormatada)));
            }

            if (entrada.getDataSaida() != null) {
                datasCell.add(new Paragraph("Data de saída: " + entrada.getDataSaida().format(dataFormatada)));
            }

            Cell totalCell = new Cell()
                    .setBorder(new SolidBorder(corPrincipal, 1))
                    .setPadding(12)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            totalCell.add(new Paragraph("VALOR TOTAL")
                    .setFontColor(corPrincipal)
                    .setFontSize(10));

            totalCell.add(new Paragraph(
                    moeda.format(entrada.getValorTotal() != null ? entrada.getValorTotal() : total)
            ).setFontSize(16)
                    .setFontColor(corTextoEscuro));

            rodapeInfo.addCell(datasCell);
            rodapeInfo.addCell(totalCell);

            document.add(rodapeInfo);

            document.add(new Paragraph(" ").setMarginBottom(20));

            // =========================
            // ASSINATURA / RODAPÉ
            // =========================
            Table assinatura = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();

            assinatura.addCell(new Cell()
                    .add(new Paragraph("Mecânica Menezes"))
                    .setBorder(Border.NO_BORDER)
                    .setFontColor(ColorConstants.GRAY));

            assinatura.addCell(new Cell()
                    .add(new Paragraph("__________________________________\nAssinatura"))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER)
                    .setFontColor(ColorConstants.GRAY));

            document.add(assinatura);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}