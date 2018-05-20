package net.oopscraft.application.bak;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import net.oopscraft.application.core.ValueMap;

//@BuiltinJobMeta(
//	name="Data Export Job",
//	description="Exort Table into CVS File",
//	type=Job.Type.SPRING,
//	command="/net/lotte/chamomile/batch/builtin/dataexport/DataExportJob.xml$dataExportJob",
//	argument="{ \r\n" + 
//	"\"table\": \"[export table name]\",\r\n" + 
//	"\"column\": [column names delimiter by ','(column1,column2...)],\r\n" + 
//	"\"where\": [where clauses(column1 = '1' AND column2 = '2'...)],\r\n" + 
//	"\"file\": \"[export file path]\"\r\n" + 
//	"}"
//)
@Configuration
public class DataExportJob {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataExportJob.class);
	
	@Autowired
	DataSource dataSource;
	
	/**
	 * returns database item reader.
	 * @return
	 * @throws Exception
	 */
	@Bean
	@StepScope
	ItemStreamReader<ValueMap> itemReader(
		 @Value("#{jobParameters['table']}")String table
		,@Value("#{jobParameters['column']}")String column
		,@Value("#{jobParameters['where']}")String where
	) throws Exception {
		JdbcCursorItemReader<ValueMap> itemReader = new JdbcCursorItemReader<ValueMap>();
		itemReader.setDataSource(dataSource);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(column);
		sql.append(" FROM ").append(table);
		if(where != null && where.trim().length() > 0) {
			sql.append(" WHERE ").append(where);
		}
		LOGGER.info("SQL:" + sql.toString());
		itemReader.setSql(sql.toString());
		itemReader.setRowMapper(new RowMapper<ValueMap>() {
			
			ResultSetMetaData rsmd = null;
			List<String> columnNames = new ArrayList<String>();
			
			@Override
			public ValueMap mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				// getting column meta info
				if(rsmd == null) {
					rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for(int i = 0; i < columnCount; i ++) {
						columnNames.add(rsmd.getColumnLabel(i+1));
					}
				}
				
				// setting map
				ValueMap row = new ValueMap();
				for(String columnName : columnNames) {
					row.set(columnName, rs.getObject(columnName));
				}
				return row;
			}
		});
		return itemReader;
	}
	
	/**
	 * returns item processor
	 * @return
	 * @throws Exception
	 */
	@Bean 
	ItemProcessor<ValueMap,ValueMap> itemProcessor() throws Exception {
		ItemProcessor<ValueMap,ValueMap> itemProcessor = new ItemProcessor<ValueMap,ValueMap>(){
			@Override
			public ValueMap process(ValueMap row) throws Exception {
				return row;
			}
		};
		return itemProcessor;
	}

	/**
	 * returns file item writer.
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@Bean
	@StepScope
	ItemStreamWriter<ValueMap> itemWriter(
		@Value("#{jobParameters['file']}")String input
	) throws Exception {
		FlatFileItemWriter<ValueMap> itemWriter = new FlatFileItemWriter<ValueMap>();
		FileSystemResource resource = new FileSystemResource(input);
		itemWriter.setResource(resource);
		itemWriter.setShouldDeleteIfExists(true);

		DelimitedLineAggregator<ValueMap> lineAggregator = new DelimitedLineAggregator<ValueMap>();
		lineAggregator.setDelimiter(",");

		PassThroughFieldExtractor<ValueMap> fieldExtractor = new PassThroughFieldExtractor<ValueMap>();
		lineAggregator.setFieldExtractor(fieldExtractor);
		itemWriter.setLineAggregator(lineAggregator);

		return itemWriter;
	}
}

