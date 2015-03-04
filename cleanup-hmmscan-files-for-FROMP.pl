#!/usr/bin/perl

# Filter the results of hmmscan using evalue, bitscores etc

#This program is free software: you can redistribute it and/or modify
#it under the terms of the GNU General Public License as published by
#the Free Software Foundation, either version 3 of the License, or
#(at your option) any later version.
 
#This program is distributed in the hope that it will be useful,
#but WITHOUT ANY WARRANTY; without even the implied warranty of
#MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#GNU General Public License for more details.
 
#You should have received a copy of the GNU General Public License
#along with this program.  If not, see <http://www.gnu.org/licenses/>.

$USAGE = q/USAGE:
perl cleanup-hmmscan-files-for-FROMP.pl        -l, --lst <filename>    file containing a list of the hmmscan output files
                                               -o, --out <\/path\/to\/output\/directory> 
                                                                       complete path to the diretory where you want to store the cleaned up hmmscan result files: 
                                                                       the output files will have ".cleaned" appended at the end
                                               -t, --type <string>     a string specifying the type of files to be processed: "modenza" or "pfam"                        
                       
                                     OPTIONAL: Filtering parameters
                                               -e, --evl <real number> The Hmmer3 Significance threshold for E-values: default 0.01; For more details refer the Hmmer 3 manual 
                                               -b, --bit <integer>     The Hmmer3 Significance threshold Bit score: default 25
                                               -s, --bias <integer>    A percentage value of the ratio of compositional bias to the bit score: helps in removing hits that occur 
                                                                       solely because of a similar compositional bias: default 5 i.e all hits with a ratio of (bias\/bit score)*100 > 5 
                                                                       would be thrown out 
                                               -m, --mod <T|F>         Choice to use the ModEnzA profiles with (T) or without (F) their optimized bitscore cutoffs: default F; 
                                                                       Using this option overrides -b, -e and -s 
                                               -c, --cut <filename>    if -m is specified then provide the file containing the ModEnzA optimized thresholds
                                               
COMMENT: Note that all the filtering parameters will be applied with an "AND"                                               
                                               
/;

use Getopt::Long;
use Data::Dumper;

GetOptions (
'l|lst=s' => \$hmmscanlist,
'o|out=s' => \$outpath,
't|type=s' => \$type,
'e|evl=s' => \$evl_cut,
'b|bit=i' => \$bit_cut,
's|bias=i' => \$bias_cut,
'm|mod=s' => \$modflag,
'c|cut=s' => \$modenza_cutfile,
) or die $USAGE;

die $USAGE if !$hmmscanlist or !$outpath or !$type;



##### SET DEFAULTS
print "Input hmmscan files are of type: $type\t";

die $USAGE if $type !~ /modenza|pfam/;

if ($type eq "modenza")
{
$modflag=!$modflag?"F":$modflag;
die $USAGE if $modflag !~ /[TF]/;
}
else
{
$modflag="";
}


die "ModEnzA selected with cutoff but no Threshold file included",$USAGE if ($modflag eq "T" and !$modenza_cutfile);

if ($type eq "modenza")
{
	if ($modflag eq "F")
	{
	print "...without optimized thresholds\n";
	($evl_cut,$bit_cut,$bias_cut)=set_defaults($evl_cut,$bit_cut,$bias_cut);
	}
	else
	{
	print "...with optimized thresholds\n";
	$bit_cut=0;
	$evl_cut=1;
	$bias_cut=5;
	}
	
}
elsif ($type eq "pfam")
{
print "...\n";
($evl_cut,$bit_cut,$bias_cut)=set_defaults($evl_cut,$bit_cut,$bias_cut);
}


sub set_defaults
{
my ($evl_cut,$bit_cut,$bias_cut)=@_;

$evl_cut=!$evl_cut?0.01:$evl_cut;
$bit_cut=!$bit_cut?25:$bit_cut;
$bias_cut=!$bias_cut?5:$bias_cut;

return ($evl_cut,$bit_cut,$bias_cut);
}

# $hmmscanlist=shift;
# chomp $hmmscanlist;
# 
# $outpath=shift;
# chomp $outpath;

open(L,$hmmscanlist);

while ($f=<L>)
{
chomp $f;
next if $f =~/\#/;
#system ("perl remove-multi-prof-hits-for-pfamscan-060711.pl $f > test.out");

print "Selecting top hits for:\t$f ...\t";

$tempfile=select_top_hits($f);

$outfile=$f;
#$outfile=~s/\.out/-wicut-cleaned\.out/g;
$outfile=$outfile.".cleaned";

print "Filtering hits...\t";

filter_hits($tempfile,$outpath,$outfile,$type,$evl_cut,$bit_cut,$bias_cut,$modflag,$modenza_cutfile);

print "Output written to $outpath\/$outfile ... \n";
system("rm test.out");
}

#system("rm test.out");


sub select_top_hits
{
my $file=shift;
my @temp=();
my %line_hash=();
my %id_hash=();

open (F,$file);
open (OUT,">test.out");
$count=1;

	while($l=<F>)
	{
	chomp $l;
	next if $l =~ /^\#/;
	$line_hash{$count}=$l;
	@temp=split(/\s+/,$l);

	$gene=$temp[0];
	$genome=$temp[1];
	$compid=$temp[2];

	#   if($compid =~/\/accession/)
	#   {
	#   @tmpid=split(/\/accession/,$compid);
	#   #$tmpid[0]=~s/_$//;
	#   chop $tmpid[0];
	#   $id=$tmpid[0];
	#   
	#   }
	#   elsif ($compid =~/length/)
	#   {
	#   @tmpid=split(/length/,$compid);
	#   #$tmpid[0]=~s/_$//;
	#   chop $tmpid[0];
	#   @tmptmpid=split(/_/,$tmpid[0]);
	#   pop @tmptmpid;
	#   $id=$tmptmpid[0];  
	#   }
		if ($compid =~ /^[0-9,A-Z,a-z,_]+_\d+/)
		{
		$fullid=$&;
		@tmpid=split(/_/,$fullid);
		$id=$tmpid[0];
		#  print "----- $id\n";
		}
		else
		{
		$id=$compid;
		#  @tmpid=split(/_/,$fullid);
		#  $id=$tmpid[0];
		#  print "----- $id\n";
		}
	#print "$id\t => $gene\t$compid\t$genome\n";    
	#print "$id\n";  
	$score=$temp[5];

	$line_hash{$count}{'id'}=$id;
	$line_hash{$count}{'fullid'}=$fullid;

		if (!exists $idhash{$id})
		{
		$idhash{$id}{'score'}=$score;
		$idhash{$id}{'line'}=$count;
		}
		elsif (exists $idhash{$id})
		{
		$idhash{$id}{'score'}=$idhash{$id}{'score'}.",".$score;
		$idhash{$id}{'line'}=$idhash{$id}{'line'}.",".$count;
		}

	$count++;
	}

	#print Dumper (\%idhash);
	#exit;

	foreach (sort {$a<=>$b} keys %line_hash)
	{
	@sortedlines=();
	%hash=();
	$id = $line_hash{$_}{'id'};
	$full_id=$line_hash{$_}{'fullid'};

	@scores=split(",",$idhash{$id}{'score'});
	#print "@scores ------- \n";
	@lines=split(",",$idhash{$id}{'line'});
	#print "@lines ------- \n";
	@hash{@lines} = @scores;
	@sortedlines = sort { $hash{$a} <=> $hash{$b} } keys %hash; 
	#print "$_\t ==== $id ....\t @sortedlines ----------\n";
	$topline=pop @sortedlines;
	#print "$_\t$id\t$topline\n";
	#print Dumper (\%hash);
		if($topline == $_)
		{
		$line=$line_hash{$_};
		print OUT $line,"\t",$full_id,"\n";
		}
	}

close F;
close OUT;

return "test.out";
}


sub filter_hits
{

my ($file,$outpath,$outfile,$type,$evl_cut,$bit_cut,$bias_cut,$modflag,$cutlist)=@_;

#print "in Filter ---\n$file\t ++ $outpath\t ++ $outfile\n";

open (OUT, ">$outpath/$outfile");

	if ($type eq "modenza" && $modflag eq "T")
	{
	open (C,$cutlist)||die "Can't open the ModEnzA cutoff file\n";

		while($c =<C>)
		{
		#print ") $c\n";
		chomp $c;
		@tmp=split("\t",$c);
		$hmm=$tmp[0];
		$hmm=~s/\.-\.ModE//g;
		$hmm=~s/\.hmm//g;
		$cutoffhash{$hmm}=$tmp[1];
		}
	close C;
	}
	
#print Dumper (\%cutoffhash);
open (HMM,$file);
	
	while ($h=<HMM>)
	{
	chomp $h;
	next if $h=~/\#/g;
	@temp=split(/\s+/,$h);
	$profid=$temp[0];
	$profid=~s/\.tp\.prof//g;
	
		if ($type eq "modenza" && $modflag eq "T")
		{
		$cutscore=$cutoffhash{$profid};
		#print "mode $profid\t$cutscore\n";
		}
		elsif ($type eq "modenza" && $modflag eq "F")
		{
		$cutscore=$bit_cut;
		}
		elsif ($type eq "pfam")
		{
		$cutscore=$bit_cut;
		}
		
	
	$bitscore=$temp[5];
	$evalue=$temp[4];

	$bias=$temp[6];

	#$bias_percentage=$bias > 0 ?(($bias/$bitscore)*100):100;

		if ($bias == 0)
		{
		$bias_percentage=0;
		}
		elsif ($bitscore == 0)
		{
		$bias_percentage=100;
		}
		else
		{
		$bias_percentage=(($bias/$bitscore)*100);
		}


	#print OUT "===== $cutscore\t$bitscore\t$evl_cut\t$evalue\t$bias\t$bias_cut\t$bias_percentage\n";

		if ($bitscore >= $cutscore && $evalue <= $evl_cut && $bias_percentage <= $bias_cut) # for modenza
		#if ($evalue <= 1e-5 && $bias_percentage <= 30) #for pfam
		{
		print OUT "$h\n";
		}
	}

close HMM;
close OUT;
}