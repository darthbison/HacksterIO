from django.shortcuts import get_object_or_404, render
from django.template import RequestContext, loader
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from .models import Question
import sys
import subprocess as sp

photonOff="curl https://api.spark.io/v1/devices/310034000347343138333038/ledOff -d access_token= -d params=off"

photonRobot="curl https://api.spark.io/v1/devices/310034000347343138333038/ledGreen -d access_token= -d params=on"

photonSecurity="curl https://api.spark.io/v1/devices/310034000347343138333038/ledRed -d access_token= -d params=on"

photonComm="curl https://api.spark.io/v1/devices/310034000347343138333038/ledBlue -d access_token= -d params=on"

photonLife="curl https://api.spark.io/v1/devices/310034000347343138333038/ledGold -d access_token= -d params=on"

runRobot="ssh pi@brickpi 'python ~/MJ/LobotArm.py'"
runSMS="java -jar ~/lobot/jars/LobotSmsSender.jar"
runEmg="java -jar ~/lobot/jars/LobotEmgCall.jar"

def robot():
	message="Activating Robotic Arm"
	sp.call("~/scripts/speak.sh "+message,shell=True)
	sp.call(photonRobot, shell=True)
        sp.call(runRobot, shell=True)

def security(request):
	message="Activating Security Cameras"
        sp.call("~/scripts/speak.sh "+message,shell=True)
	sp.call(photonSecurity, shell=True)

def comm():
	message="Relaying communications"
        sp.call("~/scripts/speak.sh "+message,shell=True)
	sp.call(photonComm, shell=True)
        sp.call(runSMS, shell=True)

def life():
        message="Engaging Life Support Emergency procedures"
        sp.call("~/scripts/speak.sh "+message,shell=True)
	sp.call(photonLife, shell=True)
	sp.call(runEmg, shell=True)

def off():
	message="Shutting down"
	sp.call("~/scripts/speak.sh "+message,shell=True)
        sp.call(photonOff, shell=True)

def results(request, question_id, selected_choice_id):
    question = get_object_or_404(Question, pk=question_id)
    if selected_choice_id == "2":
    	return render(request, 'controls/security.html')
    else:
    	return render(request, 'controls/results.html', {'question': question})

def vote(request, question_id):

    question = get_object_or_404(Question, pk=question_id)
    try:
        selected_choice = question.choice_set.get(pk=request.POST['choice'])
    except (KeyError, Choice.DoesNotExist):
        # Redisplay the question voting form.
        return render(request, 'controls/detail.html', {
            'question': question,
            'error_message': "You didn't select a choice.",
        })
    else:
	if selected_choice.id == 1:
		robot()
	elif selected_choice.id == 2:
		security(request)
	elif selected_choice.id == 3:
		comm()
	elif selected_choice.id == 4:
		life()
        elif selected_choice.id == 5:
	  	off()
	else:
	    	print "Invalid Result"
        # Always return an HttpResponseRedirect after successfully dealing
        # with POST data. This prevents data from being posted twice if a
        # user hits the Back button.
        return HttpResponseRedirect(reverse('controls:results', args=(question.id,selected_choice.id)))

def index(request):

    latest_question_list = Question.objects.order_by('-pub_date')[:5]
    template = loader.get_template('controls/index.html')
    context = RequestContext(request, {
        'latest_question_list': latest_question_list,
    })
    return HttpResponse(template.render(context))

def detail(request, question_id):
    try:
        question = Question.objects.get(pk=question_id)
    except Question.DoesNotExist:
        raise Http404("Question does not exist")
    return render(request, 'controls/detail.html', {'question': question})