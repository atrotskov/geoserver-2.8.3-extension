import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LeafletResourceLoader {

	private static final String TEXT_RESPONSE_FILE_NOT_FOUND = "Looks like server can't find specified file.";

	public LeafletResourceLoader() {
	}

	public void getRes(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, URISyntaxException {
		ServletContext context = request.getSession().getServletContext();
		String path = request.getParameter("path");

		String sourcePath = LeafletResourceLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI()
				.getPath() + path;
		File sourceFile = new File(sourcePath);
		FileInputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);

			String mimeType = context.getMimeType(sourcePath);
			if (mimeType == null) {
				mimeType = "text/plain";
			}

			response.setContentType(mimeType);

			outputStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException e) {
			response.getWriter().write(TEXT_RESPONSE_FILE_NOT_FOUND + e);
		} catch (IOException e) {
			response.getWriter().write(TEXT_RESPONSE_FILE_NOT_FOUND + e);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

}
