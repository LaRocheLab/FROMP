#!/usr/bin/perl

$hmmscanfile=shift;
chomp $hmmscanfile;
open(F,$hmmscanfile);

while ($l=<F>)
{
chomp $l;
@tmp=split(/\s+/,$l);
#print "$tmp[2]\n";
#$famid=$tmp[1];
	if ($tmp[1] =~ /-/)
	{
	$famid=$tmp[0];
	$famid=~s/_.*//g;
	}
	else
	{
	$famid=$tmp[1];
	}

$whole_seqid=$tmp[2];
# @tmpseqid=split(/_/,$whole_seqid);
# $seqid=join("_",$tmpseqid[0],$tmpseqid[1],$tmpseqid[2]);
# $seqid=~s/[_]+$//g;

  if ($whole_seqid =~ /^[0-9,A-Z,a-z,_,.]+_\d+/)
  {
  $seqid=$&;
  #@tmpid=split(/_/,$fullid);
  #$id=$tmpid[0];
  #print "----- $id\n";
  }

print "$famid,1,$seqid\n";
}
