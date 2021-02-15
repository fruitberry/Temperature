// 最終更新日時： 2020年7月18日 11:57

package servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybeans.ClBean;

/**
 * Servlet implementation class Cl_servlet
 */
@WebServlet("/Cl_servlet")
public class Cl_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cl_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		try {

			// Form data

			String Year  = request.getParameter("year");
			String Month = request.getParameter("month");
			String Day   = request.getParameter("day");

			String Prefecture = request.getParameter("prefecture");
			String Place_04   = request.getParameter("place_04");
			String Place_01   = request.getParameter("place_01");
			String Place_02   = request.getParameter("place_02");
			String Place_03   = request.getParameter("place_03");

			// Beans (Form用の設定)
			String  db_url = "jdbc:sqlite:C:/Users/~/Temperature/SQLite/testDB6.db";  //接続するデータベース名
			ClBean cb = new ClBean();
			cb.setYmd(Year, Month, Day);
			cb.setPrefecture(Prefecture);
			cb.makeArea(Place_04, Place_01, Place_02, Place_03);

			// Beans (データベース接続の設定)
			cb.setUrl(db_url);
			cb.makeUrl();
			cb.setQuery(cb.getArea(),cb.getYmd());
			cb.makeQuery();

			// Request
			request.setAttribute("cb", cb);

			//ServletContext
			ServletContext sc = getServletContext();

			//Dispatch

			if(Year.equals("2019") && Month.equals("2") && Day.equals("29")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}

			else if(Year.equals("2018") && Month.equals("2") && Day.equals("29")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Year.equals("2017") && Month.equals("2") && Day.equals("29")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Month.equals("2") && Day.equals("30")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Month.equals("2") && Day.equals("31")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Month.equals("4") && Day.equals("31")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Month.equals("6") && Day.equals("31")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Month.equals("9") && Day.equals("31")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else if(Month.equals("11") && Day.equals("31")) {
				sc.getRequestDispatcher("/WEB-INF/error.html")
				.forward(request, response);
			}
			else {
				sc.getRequestDispatcher("/WEB-INF/Result.jsp")
				.forward(request, response);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
