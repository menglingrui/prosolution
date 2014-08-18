package mymath;

public class xx {
	private int m;  //Լ�������ĸ���
	private int n;  //��������
	private int m1; //<=��Լ����������
	private int m2; //=��Լ����������
	private int m3; //>=��Լ����������
	private int error; //�ж��Ƿ��Ǵ����
	private int basic[];
	private int nonbasic[];
	private double a[][]; //Լ��������ϵ������
	private double minmax; //Ŀ�꺯�������ֵ����Сֵ
	
	/**
	 * 
	 * @param minmax -���������ֵ����Сֵ
	 * @param m -Լ�������ĸ���
	 * @param n -��������
	 * @param m1 -<=��Լ����������
	 * @param m2 -=��Լ����������
	 * @param m3 ->=��Լ����������
	 * @param a -Լ��������ϵ������
	 * @param x -Ŀ�꺯���ļ�ֵϵ��
	 */
	public xx(double minmax,int m,int n,int m1,int m2,int m3,double a[][],int x[])//���캯��
	{
		double value;
		this.error = 0;
		this.minmax = minmax;
		this.m = m;
		this.n = n;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
		if(m != m1+m2+m3)//Լ�����������
		{
			this.error = 1;
		}
		this.a = new double[m+2][];
		for(int i = 0; i < m+2; i++)
		{
			this.a[i] = new double[n+m+m3+1];
		}
		this.basic = new int[m+2];
		this.nonbasic = new int[n+m3+1];
		for(int i = 0; i <= m+1; i++)//��ʼ�����������ͷǻ�������
		{
			for(int j = 0; j <= n+m+m3; j++)
			{
				this.a[i][j] = 0.0;
			}
		}
		for(int j = 0; j <= n+m3; j++)
		{
			nonbasic[j] = j;
		}
		for(int i = 1,j = n+m3+1; i <= m; i++,j++)
		{
			basic[i]=j;
		}
		//�����ɳڱ������˹�����
		for(int i = m-m3+1,j = n+1; i<= m; i++,j++)
		{
			this.a[i][j]=-1;
			this.a[m+1][j]=-1;
		}
		//����Լ��ϵ�����Ҷ���
		for(int i = 1; i <= m; i++)
		{
			for(int j = 1; j <= n; j++)
			{
				value = a[i-1][j-1];
				this.a[i][j]=value;
			}
			value = a[i-1][n];
			if(value<0)
			{
				error = 1;
			}
			this.a[i][0]=value;
		}
		for(int j = 1; j <= n; j++) //����Ŀ�꺯��ϵ��
		{
			value = x[j - 1];
			this.a[0][j] = value * minmax;
		}
		 //�����˹������������һ�׶εĸ���Ŀ�꺯��
		for(int j = 1; j <= n; j++)
		{
			value = 0;
			for(int i = m1+1; i <= m; i++)
				value+=this.a[i][j];
			this.a[m+1][j]=value;
		}
		
	}
	//����enter(objrow)����Ŀ�꺯��ϵ�����ڵ���objrow��ѡȡ�������
	public int enter(int objrow)
	{
		int col = 0;
		for(int j = 1; j <= this.n + this.m3; j++)
		{
			if(this.nonbasic[j] <= this.n + this.m1 + this.m3 && this.a[objrow][j] > 10e-8)
			{
				col=j;
				break;
			}
		}
		return col;
	}
	//����leave(col)��������������ڵ���col��ѡȡ�������
	public int leave(int col)
	{
		double temp=-1;
		int row  = 0;
		for(int i = 1; i <= this.m; i++)
		{
			double val = this.a[i][col];
			if( val > 10e-8)
			{
				val = this.a[i][0]/val;
				if(val < temp || temp == -1)
				{
					row = i;
					temp = val;
				}
			}
		}
		return row;
	}
	//����swapbasic(row, col)������������row�ͷǻ�������col��λ��	
	public void swapbasic(int row,int col)
	{
		int temp = this.basic[row];
		this.basic[row] = this.nonbasic[col];
		this.nonbasic[col] = temp;
	}
	//����pivot(row, col)������������ڵ���col������������ڵ���row���洦Ԫ��a[row][col]Ϊ���ģ���ת��任
	public void pivot(int row,int col)
	{
		for(int j = 0;j <= this.n + this.m3; j++)
		{
			if(j != col)
			{
				this.a[row][j] = this.a[row][j] / this.a[row][col];
			}
		}
		this.a[row][col] = 1.0 / this.a[row][col];
		for(int i = 0; i <= this.m + 1; i++)
		{
			if(i != row)
			{
				for(int j = 0; j <= this.n + this.m3; j++)
				{
					if(j != col)
					{
						this.a[i][j] = this.a[i][j] - this.a[i][col] * this.a[row][j];
						if(Math.abs(this.a[i][j]) < 10e-8)
							this.a[i][j]=0;
					}
				}
				this.a[i][col] = -this.a[i][col] * this.a[row][col];
			}
		}
		swapbasic(row,col);
	}
	//����simplex(objrow)����Ŀ�꺯��ϵ�����ڵ���objrow��ִ��Լ����׼�����Թ滮����ĵ������㷨
	public int simplex(int objrow)
	{
		int row = 0;
		while(true)
		{
			int col = enter(objrow);
			if(col > 0)
			{
				row=leave(col);
			}
			else
			{
				return 0;
			}
			if(row > 0)
			{
				pivot(row,col);
			}
			else
			{
				return 2;
			}
		}
	}
	//�����ʼ�������н�ĵ�һ�׶ε������㷨��phase1()ʵ��
	public int phase1()
	{
		this.error = simplex(this.m + 1);
		if(this.error > 0)
		{
			return this.error;
		}
		for(int i = 1; i <= this.m; i++)
		{
			if(this.basic[i] > this.n + this.m1 + this.m3)
			{
				if(this.a[i][0] > 10e-8)
				{
					return 3;
				}
				for(int j = 1; j <= this.n; j++)
				{
					if(Math.abs(this.a[i][j]) >= 10e-8)
					{
						pivot(i,j);
						break;
					}
				}
			}
		}
		return 0;
	}
	
	public int phase2()
	{
		return simplex(0);
	}
	//������һ������Թ滮����ִ��2�׶ε������㷨
	public int compute()
	{
		if(this.error > 0)
			return this.error;
		if(this.m != this.m1)
		{
			this.error = phase1();
			if(this.error > 0)
				return this.error;
		}
		return phase2();
	}
	//����ʱִ��2�׶ε������㷨�Ĺ��к���
	public void solve()
	{
		error=compute();
		switch(error)
		{
		case 0:
			output();
			break;
		case 1:
			System.out.println("�������ݴ���!");
			break;
		case 2:
			System.out.println("�޽��!");
			break;
		case 3:
			System.out.println("�޿��н�!");
			break;
		default:
			break;
		}
	}
	//�������2�׶ε������㷨�ļ�����
	public void output()
	{
		int basicp[] = new int[n+1];
		for(int i = 0; i <= n; i++)
		{
			basicp[i] = 0;
		}
		for(int i = 1; i <= m; i++)
		{
			if(basic[i] >= 1 && basic[i] <= n)
			{
				basicp[basic[i]] = i;
			}
		}
		for(int i = 1; i <= n; i++)
		{
			System.out.print("x"+i+"=");
			if(basicp[i] != 0)
			{
				System.out.print(a[basicp[i]][0]);
			}
			else
			{
				System.out.print("0");
			}
			System.out.println();
		}
		System.out.println("����ֵ:"+-minmax*a[0][0]);
	}
//	public static void main(String[] args) {
//	double a[][] = {{1,-2,1,11},{-2,0,1,1},{-4,1,2,3}};
//	int x[] = {-3,1,1};
//	xx lp = new xx(-1,3,3,1,1,1,a,x);
//	lp.solve();
//    }
}
