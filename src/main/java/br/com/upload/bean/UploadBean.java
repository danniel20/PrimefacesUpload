package br.com.upload.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named
@ViewScoped
public class UploadBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String nomeDaImagem;
	private byte[] imagem;
	private List<String> imagens;
	
	private String tempFolder;

	private File diretorioTemp;
	
	public UploadBean(){
		this.imagens = new ArrayList<String>();
		
		ServletContext sContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		this.tempFolder = sContext.getRealPath("/temp");
		
		System.out.println(tempFolder.toString());
		
		 diretorioTemp= new File(tempFolder);
	}

	public void salvar() throws IOException{		
		salvarImagem(this.imagem);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Arquivo recebido: " + this.nomeDaImagem ));
		
		limpar();
		
		System.out.println(this.imagens.toString());
	}
	
	public void limpar(){
		this.nomeDaImagem = null;
		this.imagem = null;
	}

	public void doUpload(FileUploadEvent event) throws IOException{
		UploadedFile uploadedFile = event.getFile();
		this.nomeDaImagem = uploadedFile.getFileName(); 
		this.imagem = uploadedFile.getContents();	
	}

	public void salvarImagem(byte[] imagem) throws IOException{

		if(!diretorioTemp.exists()){
			diretorioTemp.mkdirs();
			diretorioTemp.setReadable(true);
			diretorioTemp.setWritable(true);
		}

		File arquivo = new File(diretorioTemp, this.nomeDaImagem);
		
		FileOutputStream fos = new FileOutputStream(arquivo);
		fos.write(imagem);
		fos.flush();
		fos.close();
			
		this.imagens.add(arquivo.getName());
	}

	public List<String> getImagens() {
		System.out.println("getImages(): "+this.imagens.toString());
		return this.imagens;
	}

	public byte[] getImagem() {
		return imagem;
	}

}

