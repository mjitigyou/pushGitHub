public static void main(String[] args) {

        listFiles(new File("xxxxx"), "  ");
    }

    /**
     * �ݹ��оٳ�ָ��Ŀ¼�µ�Ŀ¼
     * @param file
     * @param septor ����� ==�����߿ո� ��ʵ����������壬���������ʽ��
     */
    public static void listFiles(File file, String septor){
        if(file == null)return;
        if(null == septor || septor=="")septor="  ";
        String newSeptor = septor + "  ";
        if(file.exists() && file.isDirectory()){
            // �õ����֣�
            StringBuilder nameBuilder = new StringBuilder(septor + file.getName());
            nameBuilder.append("{\n");

            // ��ӡ��ǰĿ¼����Ŀ¼������
            File[] files = file.listFiles();
            if(files==null || files.length==0)return;
            int dirCount = 0;
            for (File temp : files) {
                // ֻ��ӡ�ļ���
                if(!temp.isDirectory())continue;
                dirCount++;
            }
            // Ŀ¼��Ŀ0  ����
            if(dirCount==0)return;
            for (File temp : files) {
                // ֻ��ӡ�ļ���
                if(!temp.isDirectory())continue;
                nameBuilder.append(newSeptor + temp.getName());
                nameBuilder.append("\n");
            }
            String endnewSeptor = newSeptor.length()>2?newSeptor.substring(0, newSeptor.length()-2):newSeptor;
            nameBuilder.append(endnewSeptor+"}\n");
            // ��ӡ���
            System.out.print(nameBuilder.toString());

            // �ݹ�
            for (File file1 : files) {
                listFiles(file1, newSeptor);
            }
        }
    }
