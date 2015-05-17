all: report.pdf

report.aux: report.tex
	pdflatex report.tex
	pdflatex report.tex
	
report.pdf: report.aux
	pdflatex report.tex
	pdflatex report.tex

clean:	
	rm -f *.aux *.lof *.log *.toc *.lot *.blg *.bbl *.gls *.glg *.glo *.out *.acr *.acn *.alg *.dvi *.out *.ist
