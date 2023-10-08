package utils;

import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@SelectPackages({"manager","model", "parser"})
@ExcludePackages("parser.jdt")

@Suite
public class AllTests {

}